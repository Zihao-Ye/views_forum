package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.Topic;
import com.viewsforum.viewsforum.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicDao {
    // 根据主题ID获取主题
    Topic findTopicByTopicID(Integer topicID);

    // 根据主题ID获取创建者
    User findCreatorByTopicID(Integer topicID);
}
