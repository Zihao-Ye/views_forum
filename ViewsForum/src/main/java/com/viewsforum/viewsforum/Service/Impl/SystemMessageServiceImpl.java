package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.SystemMessageDao;
import com.viewsforum.viewsforum.Entity.SystemMessage;
import com.viewsforum.viewsforum.Service.SystemMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemMessageServiceImpl implements SystemMessageService {
    @Autowired
    private SystemMessageDao systemMessageDao;

    // 添加系统消息
    public void addNewSystemMessage(SystemMessage systemMessage){
        systemMessageDao.addNewSystemMessage(systemMessage);
    }

    // 已读消息
    public void readOneSysTemMessage(Integer systemMessageID){
        systemMessageDao.readOneSysTemMessage(systemMessageID);
    }

    // 一键已读
    public void readAllSystemMessage(Integer userID,Integer messageType){
        systemMessageDao.readAllSystemMessage(userID,messageType);
    }

    // 获取未读系统消息
    public List<SystemMessage> getSystemMessageByUserIDAndMessageType(Integer userID, Integer messageType){
        return systemMessageDao.getSystemMessageByUserIDAndMessageType(userID,messageType);
    }
}
