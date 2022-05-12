package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.*;
import com.viewsforum.viewsforum.Service.AdminService;
import com.viewsforum.viewsforum.Service.SystemMessageService;
import com.viewsforum.viewsforum.Service.TopicService;
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
    private SystemMessageService systemMessageService;

    @Autowired
    private TopicService topicService;

    @GetMapping("/allAdmin")
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
            map.put("success",true);
            map.put("adminList", adminList);

        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/allAdminApplies")
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
            map.put("success",true);
            map.put("adminApplyList", adminApplyList);

        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/agreeApply")
    @ApiOperation("同意申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicID", value = "主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "ApplyID", value = "管理员申请ID",required = true,dataType = "int")
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

    @PostMapping("/rejectApply")
    @ApiOperation("拒绝申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicID", value = "主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "ApplyID", value = "管理员申请ID",required = true,dataType = "int")
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

    @PostMapping("/deleteAdmin")
    @ApiOperation("删除管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topicID", value = "主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "adminID", value = "管理员申请ID",required = true,dataType = "int")
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

    //todo 各种删除操作
}
