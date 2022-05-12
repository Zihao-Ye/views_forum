package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.AdminApply;
import com.viewsforum.viewsforum.Entity.SystemMessage;
import com.viewsforum.viewsforum.Entity.Topic;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Service.AdminService;
import com.viewsforum.viewsforum.Service.SystemMessageService;
import com.viewsforum.viewsforum.Service.TopicService;
import com.viewsforum.viewsforum.Service.UserService;
import com.viewsforum.viewsforum.Utils.ParamChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
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
            map.put("msg","INTERNAL_ERROR");
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    //todo 发布主题

    //todo 修改主题内容

    //todo 获取帖子列表

    //todo 关注主题

    //todo 取消关注
}
