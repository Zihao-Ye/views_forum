package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.PostDao;
import com.viewsforum.viewsforum.Entity.Post;
import com.viewsforum.viewsforum.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;

    // 获取主题下帖子列表
    public List<Post> getPostListByTopicID(Integer topicID){
        return postDao.getPostListByTopicID(topicID);
    }

    // 根据用户ID获取创建的帖子列表
    public List<Post> getCreatePostListByCreateID(Integer createID){
        return postDao.getCreatePostListByCreateID(createID);
    }
}
