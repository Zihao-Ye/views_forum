package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.Chat;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChattingDao {
    //添加聊天内容至数据库
    void addNewMessage(Chat chat);

    //删除聊天内容
    void deleteMessage(Chat chat);

    //根据双方用户ID获取聊天内容
    List<Chat> getChattingMessageByUserID(Integer uid,Integer toUid);

}
