package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.Comment;
import com.viewsforum.viewsforum.Entity.Post;
import com.viewsforum.viewsforum.Entity.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostDao {
    // 获取主题下帖子列表
    List<Post> getPostListByTopicID(Integer topicID);

    // 根据用户ID获取创建的帖子列表
    List<Post> getCreatePostListByCreateID(Integer createID);

    // 根据回复ID获取回复
    Review getReviewByReviewID(Integer reviewID);

    // 根据评论ID获取评论
    Comment getCommentByCommentID(Integer commentID);

    // 添加回复
    void addNewReview(Review review);
}
