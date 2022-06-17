package com.viewsforum.viewsforum.Controller;

import com.alibaba.fastjson.JSONObject;
import com.viewsforum.viewsforum.Entity.Chat;
import com.viewsforum.viewsforum.Service.ChattingService;
import com.viewsforum.viewsforum.Utils.Chatting;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.*;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@ServerEndpoint(value = "/websocket/{uid}")
@Component
public class SocketController {
    @Autowired
    private ChattingService chattingService;

    private static final ConcurrentHashMap<String, Session> SESSION_POOLS = new ConcurrentHashMap<>();
    private static final AtomicInteger ONLINE_NUM = new AtomicInteger();
    private List<String> getOnlineUsers() {
        return new ArrayList<>(SESSION_POOLS.keySet());
    }

    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uid") String uid) {
        Integer sendID = Integer.parseInt(uid);
        // 将加入连接的用户加入SESSION_POOLS集合
        SESSION_POOLS.put(uid, session);
        // 在线用户+1
        ONLINE_NUM.incrementAndGet();
        sendMsgByUid(new Chat(sendID,"connection succeed!"));
//        sendToAll(new Chatting(uid,"connection succeed!",getOnlineUsers()));
    }

    @OnClose
    public void onClose(@PathParam(value = "uid") String uid) {
        Integer sendID = Integer.parseInt(uid);
        // 将加入连接的用户移除SESSION_POOLS集合
        SESSION_POOLS.remove(uid);
        // 在线用户-1
        ONLINE_NUM.decrementAndGet();
        sendMsgByUid(new Chat(sendID,"connection closed!"));
//        sendToAll(new Chatting(uid,"connection closed!",getOnlineUsers()));
    }

    @OnMessage
    public void onMessage(String message, @PathParam(value = "uid") String uid) {
        log.info("Client:[{}]， Message: [{}]", uid, message);

        // 接收并解析前端消息并加上时间，最后根据是否有接收用户，区别发送所有用户还是单个用户
        Chatting chatting = JSONObject.parseObject(message, Chatting.class);
        String Time = chatting.getTime();
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        try {
            String[] tmp = uid.split("#");
            Integer sendID = Integer.parseInt(tmp[0]);
            Integer receiveID = Integer.parseInt(tmp[1]);
            Chat chat = new Chat();
            chat.setSendID(sendID);
            chat.setReceiveID(receiveID);
            chat.setChatContent(message);
            chat.setSendTime(new Timestamp(System.currentTimeMillis()));
            chattingService.addNewMessage(chat);
            log.info("message stored");

            sendMsgByUid(chat);
            log.info("message sent");
        }catch (Exception e){
            log.error(e.getMessage());
        }

        // 如果有接收用户就发送单个用户
    }

    private void sendToAll(Chat  chat) {
        //构建json消息体
        String content = JSONObject.toJSONString(chat);
        // 遍历发送所有在线用户
        SESSION_POOLS.forEach((k, session) ->  sendMessage(session, content));
    }

    private void sendMsgByUid(Chat chat) {
        sendMessage(SESSION_POOLS.get(String.valueOf(chat.getReceiveID())), JSONObject.toJSONString(chat));
    }

    private void sendMessage(Session session, String content){
        try {
            if (Objects.nonNull(session)) {
                // 使用Synchronized锁防止多次发送消息
                synchronized (session) {
                    // 发送消息
                    session.getBasicRemote().sendText(content);
                }
            }
        } catch (IOException ioException) {
            log.info("发送消息失败：{}", ioException.getMessage());
            ioException.printStackTrace();
        }
    }


    public String  getTime(){
        SimpleDateFormat date = new SimpleDateFormat();
        return date.format(System.currentTimeMillis());
    }

    @PostMapping("/")
    @ApiOperation("接收私信")
    @ApiImplicitParams({
            @ApiImplicitParam(name="sendID",value = "发送者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name="receiveID",value = "接收者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name="chatContent",value = "私聊内容",required = true,dataType = "String")
    })
    public Map<String,Object> addNewMessage(@RequestParam Integer sendID,@RequestParam Integer receiveID,@RequestParam String chatContent){
        Map<String,Object> map=new HashMap<>();
        try{
            Chat chat = new Chat();
            chat.setSendID(sendID);
            chat.setReceiveID(receiveID);
            chat.setChatContent(chatContent);
            chat.setSendTime(new Timestamp(System.currentTimeMillis()));
            chattingService.addNewMessage(chat);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/")
    @ApiOperation("查询私信")
    @ApiImplicitParams({
            @ApiImplicitParam(name="sendID",value = "发送者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name="receiveID",value = "接收者ID",required = true,dataType = "int")
    })
    public Map<String,Object> getChattingMessage(@RequestParam Integer sendID,@RequestParam Integer receiveID,@RequestParam String chatContent){
        Map<String,Object> map=new HashMap<>();
        try{
            List<Chat> chattingList=chattingService.getChattingMessageByUserID(sendID,receiveID);
            map.put("success",true);
            map.put("chattingList",chattingList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }
}
