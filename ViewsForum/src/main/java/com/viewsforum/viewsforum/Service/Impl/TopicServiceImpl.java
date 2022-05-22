package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.TopicDao;
import com.viewsforum.viewsforum.Entity.Topic;
import com.viewsforum.viewsforum.Entity.TopicFollow;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // 查询是否关注
    public TopicFollow findTopicFollowByFollowerIDAndTopicID(Integer followerID, Integer topicID){
        return topicDao.findTopicFollowByFollowerIDAndTopicID(followerID,topicID);
    }

    // 添加关注主题
    public void followTopic(TopicFollow topicFollow){
        topicDao.followTopic(topicFollow);
    }

    // 取消关注主题
    public void unFollowTopic(Integer followerID,Integer topicID){
        topicDao.unFollowTopic(followerID,topicID);
    }

    // 主题关注数+1
    public void addTopicFollowNum(Integer topicID){
        topicDao.addTopicFollowNum(topicID);
    }

    // 主题关注数-1
    public void minusTopicFollowNum(Integer topicID){
        topicDao.minusTopicFollowNum(topicID);
    }

    // 根据用户ID获取创建的主题列表
    public List<Topic> findCreateTopicListByCreateID(Integer createID){
        return topicDao.findCreateTopicListByCreateID(createID);
    }
}
