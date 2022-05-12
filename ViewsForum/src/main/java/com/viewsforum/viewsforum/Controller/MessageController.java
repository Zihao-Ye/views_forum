package com.viewsforum.viewsforum.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Api(tags = "消息相关接口")
@RestController("/message")
public class MessageController {
    //todo 获取系统消息列表

//    @GetMapping("/applyMessage")
//    @ApiOperation("申请类消息")
//    public Map<String,Object> applyMessage(@RequestParam Integer userID){
//
//    }

    //todo 已读消息

    //todo websocket实现实时通讯
}
