package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.*;
import com.viewsforum.viewsforum.Service.*;
import com.viewsforum.viewsforum.Utils.ParamChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "主题相关接口")
@RestController("/topic")
public class TopicController {
    private final ParamChecker paramChecker=new ParamChecker();

    @Autowired
    private SystemMessageService systemMessageService;

    @Autowired
    private UserService userService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostService postService;

    @PostMapping("/applyForAdmin")
    @ApiOperation("申请主题管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "topicID",value = "申请管理的主题ID",required = true,dataType = "int")
    })
    public Map<String,Object> adminApply(@RequestParam Integer userID, @RequestParam Integer topicID){
        Map<String ,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkNotNull(userID)||!paramChecker.checkNotNull(topicID)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            // 获取发起申请的用户
            User applyUser = userService.findUserByUserID(userID);
            // 获取申请的主题的创建者用户
            User topicOwner = topicService.findCreatorByTopicID(topicID);
            // 获取申请的主题
            Topic topic = topicService.findTopicByTopicID(topicID);

            // 检查是否已申请、已成为管理员
            if(adminService.findApplyByUserIDAndTopicID(userID,topicID)!=null){
                map.put("success",false);
                map.put("msg","已申请");
                return map;
            }
            if(adminService.findTopicAdminByUserIDAndTopicID(userID,topicID)!=null){
                map.put("success",false);
                map.put("msg","已成为管理员");
                return map;
            }

            // 发送申请
            AdminApply adminApply=new AdminApply();
            adminApply.setUserID(userID);
            adminApply.setTopicID(topicID);
            adminApply.setApplyTime(new Timestamp(System.currentTimeMillis()));
            adminService.addNewAdminApply(adminApply);

            // 系统向创建者管理员发送系统消息
            SystemMessage systemMessage=new SystemMessage();
            systemMessage.setUserID(topicOwner.getUserID());
            systemMessage.setMessageType(1);
            systemMessage.setMessageContent(applyUser.getUserName()+" 申请成为 "+topic.getTopicName()+" 的管理员");
            systemMessage.setMessageTime(new Timestamp(System.currentTimeMillis()));
            systemMessageService.addNewSystemMessage(systemMessage);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/allPost")
    @ApiOperation("获取帖子列表")
    @ApiImplicitParam(name = "topicID",value = "主题ID",required = true,dataType = "int")
    public Map<String,Object> allPost(@RequestParam Integer topicID){
        Map<String,Object> map=new HashMap<>();
        try {
            List<Post> postList = postService.getPostListByTopicID(topicID);
            map.put("postList",postList);
            map.put("success", true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/followTopic")
    @ApiOperation("关注主题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "topicID",value = "主题ID",required = true,dataType = "int")
    })
    public Map<String,Object> followTopic(@RequestParam Integer userID,@RequestParam Integer topicID){
        Map<String,Object> map=new HashMap<>();
        try {
            // 检查是否关注
            if(topicService.findTopicFollowByFollowerIDAndTopicID(userID,topicID)!=null){
                map.put("success",false);
                map.put("msg","已关注");
                return map;
            }

            // 获取发起申请的用户
            User follower = userService.findUserByUserID(userID);
            // 获取申请的主题的创建者用户
            User topicOwner = topicService.findCreatorByTopicID(topicID);
            // 获取关注的主题
            Topic topic = topicService.findTopicByTopicID(topicID);

            // 添加关注
            TopicFollow topicFollow = new TopicFollow();
            topicFollow.setTopicID(topicID);
            topicFollow.setFollowerID(userID);
            topicFollow.setFollowTime(new Timestamp(System.currentTimeMillis()));
            topicService.followTopic(topicFollow);

            // 关注数+1
            topicService.addTopicFollowNum(topicID);

            // 发送关注系统消息
            SystemMessage systemMessage=new SystemMessage();
            systemMessage.setUserID(topicOwner.getUserID());
            systemMessage.setMessageType(3);
            systemMessage.setMessageContent(follower.getUserName()+" 关注了您的主题 "+topic.getTopicName());
            systemMessage.setMessageTime(new Timestamp(System.currentTimeMillis()));
            systemMessageService.addNewSystemMessage(systemMessage);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/unFollowTopic")
    @ApiOperation("取消关注主题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "topicID",value = "主题ID",required = true,dataType = "int")
    })
    public Map<String,Object> unFollowTopic(@RequestParam Integer userID,@RequestParam Integer topicID){
        Map<String,Object> map=new HashMap<>();
        try {
            // 检查是否关注
            if(topicService.findTopicFollowByFollowerIDAndTopicID(userID,topicID)==null){
                map.put("success",false);
                map.put("msg","未关注");
                return map;
            }

            // 取消关注
            topicService.unFollowTopic(userID,topicID);

            // 关注数-1
            topicService.minusTopicFollowNum(topicID);

            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/isFollowTopic")
    @ApiOperation("查询是否关注主题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "topicID",value = "主题ID",required = true,dataType = "int")
    })
    public Map<String,Object> isFollowTopic(@RequestParam Integer userID,@RequestParam Integer topicID){
        Map<String,Object> map=new HashMap<>();
        try {
            boolean isFollow=topicService.findTopicFollowByFollowerIDAndTopicID(userID,topicID)!=null;
            map.put("success",true);
            map.put("isFollow",isFollow);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/searchPost")
    @ApiOperation("/搜索主题下帖子")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicID",value = "主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "keyword",value = "搜索关键词",required = true,dataType = "String")
    })
    public Map<String,Object> searchPost(@RequestParam Integer topicID,@RequestParam String keyword){
        Map<String,Object> map=new HashMap<>();
        try {
            List<Post> postList=topicService.findPostByTopicIDAndKeyword(topicID,keyword);
            map.put("success",true);
            map.put("postList",postList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/searchTopic")
    @ApiOperation("/搜索主题")
    @ApiImplicitParam(name = "keyword",value = "搜索关键词",required = true,dataType = "String")
    public Map<String,Object> searchTopic(@RequestParam String keyword){
        Map<String,Object> map=new HashMap<>();
        try {
            List<Topic> topicList=topicService.findTopicByKeyword(keyword);
            map.put("success",true);
            map.put("topicList",topicList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/addTopic")
    @ApiOperation("发布主题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "创建者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "topicName",value = "主题名，1<=字符长度<=15",required = true,dataType = "String"),
            @ApiImplicitParam(name = "topicNote",value = "主题说明，1<=字符长度<=201",required = true,dataType = "String")
    })
    public Map<String,Object> addTopic(@RequestParam Integer userID, @RequestParam String topicName,@RequestParam String topicNote){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkTopicName(topicName)){
                map.put("success",false);
                map.put("msg","主题名格式错误");
                return map;
            }
            if(!paramChecker.checkNote(topicNote)){
                map.put("success",false);
                map.put("msg","主题说明格式错误");
                return map;
            }
            if(topicService.findTopicByTopicName(topicName)!=null){
                map.put("success",false);
                map.put("msg","主题名已存在");
                return map;
            }
            Topic topic=new Topic();
            topic.setCreateID(userID);
            topic.setTopicName(topicName);
            topic.setTopicNote(topicNote);
            topic.setCreateTime(new Timestamp(System.currentTimeMillis()));
            topicService.addNewTopic(topic);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/editTopic")
    @ApiOperation("修改主题信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "topicID",value = "主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "topicName",value = "主题名，1<=字符长度<=15",required = true,dataType = "String"),
            @ApiImplicitParam(name = "topicNote",value = "主题说明，1<=字符长度<=201",required = true,dataType = "String")
    })
    public Map<String,Object> editTopic(@RequestParam Integer userID,@RequestParam Integer topicID, @RequestParam String topicName,@RequestParam String topicNote){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkTopicName(topicName)){
                map.put("success",false);
                map.put("msg","主题名格式错误");
                return map;
            }
            if(!paramChecker.checkNote(topicNote)){
                map.put("success",false);
                map.put("msg","主题说明格式错误");
                return map;
            }
            if(adminService.findTopicCreatorByUserIDAndTopicID(userID,topicID)==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }
            Topic topic=topicService.findTopicByTopicName(topicName);
            if(topic!=null&& !topic.getTopicID().equals(topicID)){
                map.put("success",false);
                map.put("msg","主题名已存在");
                return map;
            }
            topicService.editTopicByTopicID(topicID,topicName,topicNote);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }
}
