package com.viewsforum.viewsforum.Service;

import com.viewsforum.viewsforum.Entity.Post;

import java.util.List;

public interface PostService {
    // 获取主题下帖子列表
    List<Post> getPostListByTopicID(Integer topicID);

    // 根据用户ID获取创建的帖子列表
    List<Post> getCreatePostListByCreateID(Integer createID);
}
