package com.viewsforum.viewsforum.Service;

import com.viewsforum.viewsforum.Entity.Topic;
import com.viewsforum.viewsforum.Entity.User;

public interface TopicService {
    // 根据主题ID获取主题
    Topic findTopicByTopicID(Integer topicID);

    // 根据主题ID获取创建者
    User findCreatorByTopicID(Integer topicID);
}
