package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostDao {
    // 获取主题下帖子列表
    List<Post> getPostListByTopicID(Integer topicID);
}
