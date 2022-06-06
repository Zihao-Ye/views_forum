package com.viewsforum.viewsforum.Service;

import com.viewsforum.viewsforum.Entity.SystemMessage;

import java.util.List;

public interface SystemMessageService {
    // 添加系统消息
    void addNewSystemMessage(SystemMessage systemMessage);

    // 已读消息
    void readOneSysTemMessage(Integer systemMessageID);

    // 一键已读(类别)
    void readAllSystemMessageByType(Integer userID,Integer messageType);

    // 获取未读系统消息（类别）
    List<SystemMessage> getSystemMessageByUserIDAndMessageType(Integer userID,Integer messageType);

    // 获取所有未读系统消息
    List<SystemMessage> getAllSystemMessageByUserID(Integer userID);

    //一键已读（所有）
    void readAllSystemMessage(Integer userID);
}
