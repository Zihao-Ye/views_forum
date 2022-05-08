package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.TopicDao;
import com.viewsforum.viewsforum.Entity.Topic;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicDao topicDao;

    // 根据主题ID获取主题
    public Topic findTopicByTopicID(Integer topicID){
        return topicDao.findTopicByTopicID(topicID);
    }

    // 根据主题ID获取创建者
    public User findCreatorByTopicID(Integer topicID){
        return topicDao.findCreatorByTopicID(topicID);
    }
}
