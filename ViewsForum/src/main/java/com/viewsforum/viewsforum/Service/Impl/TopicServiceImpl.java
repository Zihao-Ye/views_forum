package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.TopicDao;
import com.viewsforum.viewsforum.Entity.Post;
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

    // 根据主题ID与关键词搜索帖子
    public List<Post> findPostByTopicIDAndKeyword(Integer topicID, String keyword){
        return topicDao.findPostByTopicIDAndKeyword(topicID,keyword);
    }

    // 根据关键词搜索主题
    public List<Topic> findTopicByKeyword(String keyword){
        return topicDao.findTopicByKeyword(keyword);
    }

    // 根据主题名查询主题
    public Topic findTopicByTopicName(String topicName){
        return topicDao.findTopicByTopicName(topicName);
    }

    // 发布主题
    public void addNewTopic(Topic topic){
        topicDao.addNewTopic(topic);
    }

    // 修改主题信息
    public void editTopicByTopicID(Integer topicID,String topicName,String topicNote){
        topicDao.editTopicByTopicID(topicID,topicName,topicNote);
    }
}
