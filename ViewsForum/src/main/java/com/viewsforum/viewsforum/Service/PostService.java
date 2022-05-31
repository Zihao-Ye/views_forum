package com.viewsforum.viewsforum.Service;

import com.viewsforum.viewsforum.Entity.Comment;
import com.viewsforum.viewsforum.Entity.Post;
import com.viewsforum.viewsforum.Entity.Review;

import java.util.List;

public interface PostService {
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

    // 回复数+1
    void addReviewNum(Integer commentID);

    // 评论数+1
    void addCommentNum(Integer postID);

    // 回复数-1
    void minusReviewNum(Integer commentID);

    // 评论数-1
    void minusCommentNum(Integer postID);

    // 添加帖子
    void addNewPost(Post post);

    // 添加回复
    void addNewComment(Comment comment);

    // 根据帖子ID获取帖子
    Post getPostByPostID(Integer postID);

    // 根据帖子ID获取帖子下评论列表
    List<Comment> getCommentListByPostID(Integer postID);

    // 根据评论ID获取评论下回复列表
    List<Review> getReviewListByCommentID(Integer commentID);
}
