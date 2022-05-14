package com.viewsforum.viewsforum.Service;

import com.viewsforum.viewsforum.Entity.Post;

import java.util.List;

public interface PostService {
    // 获取主题下帖子列表
    List<Post> getPostListByTopicID(Integer topicID);
}
