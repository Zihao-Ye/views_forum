package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.SystemMessage;
import com.viewsforum.viewsforum.Service.SystemMessageService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "消息相关接口")
@RestController("/message")
public class MessageController {
    @Autowired
    private SystemMessageService systemMessageService;

    @GetMapping("differentMessage")//已测试
    @ApiOperation("不同类别的消息（按类别）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "messageType",value = "消息类型 1：申请信息（创建者接收申请信息、申请者接收到拒绝/同意/被解除管理员信息）2：回复信息（帖子创建者接收到发言信息、发言/回复接收到回复信息） 3：关注信息（被关注信息） 4：警告/删除信息（主题被删除、帖子被删除、发言被删除、回复被删除）",required = true,dataType = "int")
    })
    public Map<String,Object> differentMessage(@RequestParam Integer userID,@RequestParam Integer messageType){
        Map<String,Object> map=new HashMap<>();
        try {
            List<SystemMessage> systemMessageList=systemMessageService.getSystemMessageByUserIDAndMessageType(userID,messageType);
            map.put("success",true);
            map.put("systemMessageList",systemMessageList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("readOneMessage")//已测试
    @ApiOperation("已读消息")
    @ApiImplicitParam(name = "systemMessageID",value = "系统消息ID",required = true,dataType = "int")
    public Map<String,Object> readOneMessage(@RequestParam Integer systemMessageID){
        Map<String,Object> map=new HashMap<>();
        try {
            systemMessageService.readOneSysTemMessage(systemMessageID);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("readAllMessageByType")//已测试
    @ApiOperation("一键已读消息(按类别)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "messageType",value = "消息类型 1：申请信息（创建者接收申请信息、申请者接收到拒绝/同意/被解除管理员信息）2：回复信息（帖子创建者接收到发言信息、发言/回复接收到回复信息） 3：关注信息（被关注信息） 4：警告/删除信息（主题被删除、帖子被删除、发言被删除、回复被删除）",required = true,dataType = "int")
    })
    public Map<String,Object> readAllMessageByType(@RequestParam Integer userID,@RequestParam Integer messageType){
        Map<String,Object> map=new HashMap<>();
        try {
            systemMessageService.readAllSystemMessageByType(userID,messageType);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("allMessage")//已测试
    @ApiOperation("不同类别的消息（所有的）")
    @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int")
    public Map<String,Object> allMessage(@RequestParam Integer userID){
        Map<String,Object> map=new HashMap<>();
        try {
            List<SystemMessage> systemMessageList=systemMessageService.getAllSystemMessageByUserID(userID);
            map.put("success",true);
            map.put("systemMessageList",systemMessageList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("readAllMessage")//已测试
    @ApiOperation("一键已读消息")
    @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int")
    public Map<String,Object> readAllMessage(@RequestParam Integer userID){
        Map<String,Object> map=new HashMap<>();
        try {
            systemMessageService.readAllSystemMessage(userID);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }
}
