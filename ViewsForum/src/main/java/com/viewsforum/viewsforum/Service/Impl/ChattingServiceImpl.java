package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.ChattingDao;
import com.viewsforum.viewsforum.Entity.Chat;
import com.viewsforum.viewsforum.Service.ChattingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChattingServiceImpl implements ChattingService {
    @Autowired
    private ChattingDao chattingDao;

    public void addNewMessage(Chat chat){
        chattingDao.addNewMessage(chat);
    }

    public void deleteMessage(Chat chat){
        chattingDao.deleteMessage(chat);
    }

    public List<Chat> getChattingMessageByUserID(Integer uid,Integer toUid){
        return chattingDao.getChattingMessageByUserID(uid,toUid);
    }
}
