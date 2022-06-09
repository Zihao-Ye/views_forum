package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.Post;
import com.viewsforum.viewsforum.Entity.Topic;
import com.viewsforum.viewsforum.Entity.TopicFollow;
import com.viewsforum.viewsforum.Entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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

    // 根据用户ID获取创建的主题列表
    List<Topic> findCreateTopicListByCreateID(Integer createID);

    // 根据主题ID与关键词搜索帖子
    List<Post> findPostByTopicIDAndKeyword(Integer topicID, String keyword);

    // 根据关键词搜索主题
    List<Topic> findTopicByKeyword(String keyword);

    // 根据主题名查询主题
    Topic findTopicByTopicName(String topicName);

    // 发布主题
    void addNewTopic(Topic topic);

    // 修改主题信息
    void editTopicByTopicID(Integer topicID,String topicName,String topicNote);

    // 贴子数+1
    void addTopicPostNum(Integer topicID);

    // 贴子数-1
    void minusTopicPostNum(Integer topicID);
}
