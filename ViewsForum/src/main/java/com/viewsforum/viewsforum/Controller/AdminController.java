package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.*;
import com.viewsforum.viewsforum.Pojo.AdminControllerPojo.AdminApplyUser;
import com.viewsforum.viewsforum.Pojo.AdminControllerPojo.AdminUser;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "管理员操作相关接口")
@RestController("/admin")
public class AdminController {
    private final ParamChecker paramChecker=new ParamChecker();

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemMessageService systemMessageService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private PostService postService;

    @GetMapping("/allAdmin")//已测试
    @ApiOperation("获取主题的所有管理员")
    @ApiImplicitParam(name = "topicID", value = "主题ID",required = true,dataType = "int")
    public Map<String,Object> allAdmin(HttpServletRequest request, @RequestParam Integer topicID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkNotNull(topicID)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            if(adminService.findTopicCreatorByUserIDAndTopicID(userID,topicID)==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            List<Admin> adminList = adminService.findAllAdminByTopicID(topicID);
            List<AdminUser> adminUserList=new ArrayList<>();
            for(Admin admin:adminList){
                Integer thisUserID=admin.getUserID();
                AdminUser adminUser=new AdminUser();
                adminUser.setAdminID(admin.getAdminID());
                adminUser.setUserID(thisUserID);
                adminUser.setAdminType(admin.getAdminType());
                adminUser.setUserName(userService.findUserByUserID(thisUserID).getUserName());
                adminUserList.add(adminUser);
            }
            map.put("success",true);
            map.put("adminUserList", adminUserList);

        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/allAdminApplies")//已测试
    @ApiOperation("获取主题的管理员申请")
    @ApiImplicitParam(name = "topicID", value = "主题ID",required = true,dataType = "int")
    public Map<String,Object> allAdminApplies(HttpServletRequest request, @RequestParam Integer topicID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkNotNull(topicID)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            if(adminService.findTopicCreatorByUserIDAndTopicID(userID,topicID)==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            List<AdminApply> adminApplyList = adminService.findAllAdminApplyByTopicID(topicID);
            List<AdminApplyUser> adminApplyUserList=new ArrayList<>();
            for(AdminApply adminApply:adminApplyList){
                Integer thisUserID=adminApply.getUserID();
                AdminApplyUser adminApplyUser=new AdminApplyUser();
                adminApplyUser.setApplyID(adminApply.getApplyID());
                adminApplyUser.setApplyTime(adminApply.getApplyTime());
                adminApplyUser.setUserName(userService.findUserByUserID(thisUserID).getUserName());
                adminApplyUser.setUserID(userService.findUserByUserID(thisUserID).getUserID());
                adminApplyUserList.add(adminApplyUser);
            }
            map.put("success",true);
            map.put("adminApplyUserList", adminApplyUserList);

        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/agreeApply")//已测试
    @ApiOperation("同意申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicID", value = "主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "applyID", value = "管理员申请ID",required = true,dataType = "int")
    })
    public Map<String,Object> agreeApply(HttpServletRequest request, @RequestParam Integer topicID,@RequestParam Integer applyID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkNotNull(applyID)||!paramChecker.checkNotNull(topicID)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            if(adminService.findTopicCreatorByUserIDAndTopicID(userID,topicID)==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            AdminApply adminApply = adminService.findApplyByApplyID(applyID);
            Topic topic = topicService.findTopicByTopicID(topicID);

            // 设置申请已处理
            adminService.finishAdminApplyByApplyID(applyID);

            // 设置管理员
            Admin admin =new Admin();
            admin.setUserID(adminApply.getUserID());
            admin.setTopicID(topicID);
            admin.setAdminType(2);
            adminService.addNewTopicAdmin(admin);

            // 发送消息
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setUserID(adminApply.getUserID());
            systemMessage.setMessageType(1);
            systemMessage.setMessageContent("您已成为主题 "+topic.getTopicName()+" 的管理员");
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

    @PostMapping("/rejectApply")//已测试
    @ApiOperation("拒绝申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicID", value = "主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "applyID", value = "管理员申请ID",required = true,dataType = "int")
    })
    public Map<String,Object> rejectApply(HttpServletRequest request, @RequestParam Integer topicID,@RequestParam Integer applyID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkNotNull(applyID)||!paramChecker.checkNotNull(topicID)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            if(adminService.findTopicCreatorByUserIDAndTopicID(userID,topicID)==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            AdminApply adminApply = adminService.findApplyByApplyID(applyID);
            Topic topic = topicService.findTopicByTopicID(topicID);

            // 设置申请已处理
            adminService.finishAdminApplyByApplyID(applyID);

            // 发送消息
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setUserID(adminApply.getUserID());
            systemMessage.setMessageType(1);
            systemMessage.setMessageContent("您被拒绝成为主题 "+topic.getTopicName()+" 的管理员");
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

    @PostMapping("/deleteAdmin")//已测试
    @ApiOperation("删除管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicID", value = "主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "adminID", value = "管理员ID",required = true,dataType = "int")
    })
    public Map<String,Object> deleteAdmin(HttpServletRequest request, @RequestParam Integer topicID,@RequestParam Integer adminID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkNotNull(adminID)||!paramChecker.checkNotNull(topicID)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            if(adminService.findTopicCreatorByUserIDAndTopicID(userID,topicID)==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            Admin admin = adminService.findAdminByAdminID(adminID);
            Topic topic = topicService.findTopicByTopicID(topicID);

            // 发送消息
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setUserID(admin.getUserID());
            systemMessage.setMessageType(1);
            systemMessage.setMessageContent("您被取消主题 "+topic.getTopicName()+" 的管理员身份");
            systemMessage.setMessageTime(new Timestamp(System.currentTimeMillis()));
            systemMessageService.addNewSystemMessage(systemMessage);

            // 取消管理员身份
            adminService.deleteAdmin(adminID);

            map.put("success",true);

        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/deleteTopic")
    @ApiOperation("删除主题")
    @ApiImplicitParam(name = "topicID",value = "主题ID",required = true,dataType = "int")
    public Map<String,Object> deleteTopic(HttpServletRequest request, @RequestParam Integer topicID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            if(adminService.findSystemAdminByUserID(userID)==null&&adminService.findTopicCreatorByUserIDAndTopicID(userID,topicID)==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            adminService.deleteTopic(topicID);

            Topic topic=topicService.findTopicByTopicID(topicID);
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setUserID(topic.getCreateID());
            systemMessage.setMessageType(4);
            systemMessage.setMessageContent("您创建的主题 "+topic.getTopicName()+" 因违规被删除，请联系管理员");
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

    @PostMapping("/deletePost")
    @ApiOperation("删除帖子")
    @ApiImplicitParam(name = "postID",value = "帖子ID",required = true,dataType = "int")
    public Map<String,Object> deletePost(HttpServletRequest request, @RequestParam Integer postID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            Post post=postService.getPostByPostID(postID);
            if(adminService.findTopicAdminByUserIDAndTopicID(userID,post.getTopicID())==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            adminService.deletePost(postID);

            topicService.minusTopicPostNum(post.getTopicID());

            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setUserID(post.getCreateID());
            systemMessage.setMessageType(4);
            systemMessage.setMessageContent("您创建的的帖子 "+post.getPostName()+" 因违规被删除，请联系管理员");
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

    @PostMapping("/deleteComment")
    @ApiOperation("删除评论")
    @ApiImplicitParam(name = "commentID",value = "评论ID",required = true,dataType = "int")
    public Map<String,Object> deleteComment(HttpServletRequest request, @RequestParam Integer commentID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            Comment comment=postService.getCommentByCommentID(commentID);
            Post post=postService.getPostByPostID(comment.getPostID());
            if(adminService.findTopicCreatorByUserIDAndTopicID(userID,post.getTopicID())==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            adminService.deleteComment(commentID);

            postService.minusCommentNum(post.getPostID());

            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setUserID(comment.getCreateID());
            systemMessage.setMessageType(4);
            systemMessage.setMessageContent("您创建的评论 "+comment.getCommentContent()+" 因违规被删除，请联系管理员");
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

    @PostMapping("/deleteReview")
    @ApiOperation("删除回复")
    @ApiImplicitParam(name = "reviewID",value = "回复ID",required = true,dataType = "int")
    public Map<String,Object> deleteReview(HttpServletRequest request, @RequestParam Integer reviewID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(request.getSession().getAttribute("userID")==null){
                map.put("success",false);
                map.put("msg","未登录");
                return map;
            }
            Integer userID = (Integer) request.getSession().getAttribute("userID");
            Review review=postService.getReviewByReviewID(reviewID);
            Comment comment=postService.getCommentByCommentID(review.getCommentID());
            Post post=postService.getPostByPostID(comment.getPostID());
            if(adminService.findTopicCreatorByUserIDAndTopicID(userID,post.getTopicID())==null){
                map.put("success",false);
                map.put("msg","权限不足");
                return map;
            }

            adminService.deleteReview(reviewID);

            postService.minusReviewNum(comment.getCommentID());

            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setUserID(review.getFromID());
            systemMessage.setMessageType(4);
            systemMessage.setMessageContent("您创建的回复 "+review.getReviewContent()+" 因违规被删除，请联系管理员");
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
}
