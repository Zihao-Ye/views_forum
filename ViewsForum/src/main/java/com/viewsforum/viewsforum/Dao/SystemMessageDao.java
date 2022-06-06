package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.SystemMessage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemMessageDao {
    // 添加系统消息
    void addNewSystemMessage(SystemMessage systemMessage);

    // 已读消息
    void readOneSysTemMessage(Integer systemMessageID);

    // 一键已读（类别）
    void readAllSystemMessageByType(Integer userID,Integer messageType);

    // 获取未读系统消息（分类）
    List<SystemMessage> getSystemMessageByUserIDAndMessageType(Integer userID, Integer messageType);

    // 获取所有未读系统消息
    List<SystemMessage> getAllSystemMessageByUserID(Integer userID);

    //一键已读（所有）
    void readAllSystemMessage(Integer userID);
}
