package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.Topic;
import com.viewsforum.viewsforum.Entity.TopicFollow;
import com.viewsforum.viewsforum.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicDao {
    // 根据主题ID获取主题
    Topic findTopicByTopicID(Integer topicID);

    // 根据主题ID获取创建者
    User findCreatorByTopicID(Integer topicID);

    // 查询是否关注
    TopicFollow findTopicFollowByFollowerIDAndTopicID(Integer followerID, Integer topicID);

    // 添加关注主题
    void followTopic(TopicFollow topicFollow);

    // 取消关注主题
    void unFollowTopic(Integer followerID,Integer topicID);

    // 主题关注数+1
    void addTopicFollowNum(Integer topicID);

    // 主题关注数-1
    void minusTopicFollowNum(Integer topicID);
}
