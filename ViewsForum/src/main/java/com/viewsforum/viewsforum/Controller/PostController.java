package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.*;
import com.viewsforum.viewsforum.Pojo.PostControllerPojo.DetailComment;
import com.viewsforum.viewsforum.Pojo.PostControllerPojo.DetailReview;
import com.viewsforum.viewsforum.Service.PostService;
import com.viewsforum.viewsforum.Service.SystemMessageService;
import com.viewsforum.viewsforum.Service.TopicService;
import com.viewsforum.viewsforum.Service.UserService;
import com.viewsforum.viewsforum.Utils.ParamChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Api(tags = "帖子相关接口")
@RestController("/post")
public class PostController {
    private final ParamChecker paramChecker=new ParamChecker();

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private SystemMessageService systemMessageService;

    @PostMapping("/addPost")//已测试
    @ApiOperation("创建帖子")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "postName",value = "帖子名称，1<=字符长度<=20",required = true,dataType = "String"),
            @ApiImplicitParam(name = "topicID",value = "帖子所属主题ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "img",value = "图片",required = false,dataType = "MultipartFile"),
            @ApiImplicitParam(name = "hasFile",value = "是否上传图片，0：不上传，1：上传",required = true,dataType = "int")
    })
    public Map<String,Object> addPost(@RequestParam Integer userID, @RequestParam String postName, @RequestParam Integer topicID,@RequestParam(required = false) MultipartFile img,@RequestParam Integer hasFile){
        Map<String,Object> map=new HashMap<>();
        if(!paramChecker.checkPostName(postName)){
            map.put("success",false);
            map.put("msg","帖子名称格式错误");
            return map;
        }
        if(hasFile!=0&&hasFile!=1){
            map.put("success",false);
            map.put("msg","hasFile参数错误");
            return map;
        }
        if(hasFile==1&&img.isEmpty()){
            map.put("success",false);
            map.put("msg","上传失败");
            return map;
        }
        try {
            Post post=new Post();
            post.setPostName(postName);
            post.setCreateID(userID);
            post.setTopicID(topicID);
            post.setPostTime(new Timestamp(System.currentTimeMillis()));
            post.setCommentNum(0);
            post.setIsDelete(0);
            if(hasFile==0){
                post.setPicturePath("null");
                postService.addNewPost(post);
                topicService.addTopicPostNum(topicID);
            }
            else{
                try {
                    String filename=img.getOriginalFilename();
                    String newFilename= UUID.randomUUID()+filename;
                    File dest=new File(newFilename);
                    img.transferTo(dest);
                    String picturePath=dest.getPath();
                    post.setPicturePath(picturePath);
                    postService.addNewPost(post);
                    topicService.addTopicPostNum(topicID);
                }catch (IOException ioException){
                    log.error(ioException.getMessage());
                    map.put("success",false);
                    map.put("msg","IO_ERROR");
                }
            }
            map.put("success",true);
            map.put("post",post);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/addComment")//已测试
    @ApiOperation("创建评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "commentContent",value = "评论内容,1<=字符长度<=200",required = true,dataType = "String"),
            @ApiImplicitParam(name = "postID",value = "评论所属帖子ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "img",value = "图片",required = false,dataType = "MultipartFile"),
            @ApiImplicitParam(name = "hasFile",value = "是否上传图片，0：不上传，1：上传",required = true,dataType = "int")
    })
    public Map<String,Object> addComment(@RequestParam Integer userID, @RequestParam String commentContent, @RequestParam Integer postID,@RequestParam(required = false) MultipartFile img,@RequestParam Integer hasFile){
        Map<String,Object> map=new HashMap<>();
        if(!paramChecker.checkContent(commentContent)){
            map.put("success",false);
            map.put("msg","评论内容格式错误");
            return map;
        }
        if(hasFile!=0&&hasFile!=1){
            map.put("success",false);
            map.put("msg","hasFile参数错误");
            return map;
        }
        if(hasFile==1&&img.isEmpty()){
            map.put("success",false);
            map.put("msg","上传失败");
            return map;
        }
        try {
            Comment comment=new Comment();
            comment.setCommentContent(commentContent);
            comment.setCreateID(userID);
            comment.setPostID(postID);
            comment.setCommentTime(new Timestamp(System.currentTimeMillis()));
            comment.setIsDelete(0);
            comment.setReviewNum(0);
            if(hasFile==0){
                comment.setPicturePath("null");
                postService.addNewComment(comment);
            }
            else{
                try {
                    String filename=img.getOriginalFilename();
                    String newFilename= UUID.randomUUID()+filename;
                    File dest=new File(newFilename);
                    img.transferTo(dest);
                    String picturePath=dest.getPath();
                    comment.setPicturePath(picturePath);
                    postService.addNewComment(comment);
                }catch (IOException ioException){
                    log.error(ioException.getMessage());
                    map.put("success",false);
                    map.put("msg","IO_ERROR");
                }
            }
            Post post= postService.getPostByPostID(postID);
            SystemMessage systemMessage=new SystemMessage();
            systemMessage.setMessageTime(new Timestamp(System.currentTimeMillis()));
            systemMessage.setMessageType(2);
            systemMessage.setUserID(post.getCreateID());
            systemMessage.setMessageContent(userService.findUserByUserID(userID).getUserName()+" 评论了您的帖子 "+post.getPostName());
            systemMessageService.addNewSystemMessage(systemMessage);

            postService.addCommentNum(postID);

            map.put("success",true);
            map.put("comment",comment);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/reviewToCommentOrReview")//已测试
    @ApiOperation("回复评论或回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromID",value = "回复者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "toID",value = "被回复者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "commentID",value = "回复所属评论ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "reviewedID",value = "被回复的评论ID/回复ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "reviewType",value = "回复种类： 1：回复发言 2：回复回复",required = true,dataType = "int"),
            @ApiImplicitParam(name = "reviewContent",value = "回复内容，1<=字符长度<=200",required = true,dataType = "String")
    })
    public Map<String,Object> reviewToCommentOrReview(@RequestParam Integer fromID,@RequestParam Integer toID,@RequestParam Integer commentID,@RequestParam Integer reviewedID,@RequestParam Integer reviewType,@RequestParam String reviewContent){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkContent(reviewContent)){
                map.put("success",false);
                map.put("msg","回复格式错误");
                return map;
            }
            User fromUser=userService.findUserByUserID(fromID);
            User toUser=userService.findUserByUserID(toID);

            Review review=new Review();
            review.setReviewContent(reviewContent);
            review.setCommentID(commentID);
            review.setReviewedID(reviewedID);
            review.setFromID(fromID);
            review.setToID(toID);
            review.setReviewTime(new Timestamp(System.currentTimeMillis()));
            review.setReviewType(reviewType);
            postService.addNewReview(review);

            postService.addReviewNum(commentID);

            String messageContent;
            if(reviewType==1){
                Comment comment= postService.getCommentByCommentID(reviewedID);
                messageContent=fromUser.getUserName()+" 回复了您的评论 "+comment.getCommentContent();
            }
            else{
                Review review1 = postService.getReviewByReviewID(reviewedID);
                messageContent=fromUser.getUserName()+" 回复了您的回复 "+review1.getReviewContent();
            }
            SystemMessage systemMessage=new SystemMessage();
            systemMessage.setUserID(toID);
            systemMessage.setMessageType(2);
            systemMessage.setMessageTime(new Timestamp(System.currentTimeMillis()));
            systemMessage.setMessageContent(messageContent);
            systemMessageService.addNewSystemMessage(systemMessage);

            DetailReview detailReview=new DetailReview();
            detailReview.setFromID(fromID);
            detailReview.setFromName(fromUser.getUserName());
            detailReview.setToID(toID);
            detailReview.setToName(toUser.getUserName());
            detailReview.setReviewContent(reviewContent);
            detailReview.setReviewID(review.getReviewID());
            detailReview.setReviewType(reviewType);
            detailReview.setReviewTime(review.getReviewTime());

            map.put("success",true);
            map.put("detailReview",detailReview);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/showCommentAndReviews")//已测试
    @ApiOperation("获取帖子下的评论及其回复")
    @ApiImplicitParam(name = "postID",value = "帖子ID",required = true,dataType = "int")
    public Map<String,Object> showCommentAndReviews(@RequestParam Integer postID){
        Map<String,Object> map=new HashMap<>();
        try {
            List<Comment> commentList= postService.getCommentListByPostID(postID);
            List<DetailComment> detailCommentList=new ArrayList<>();
            for(Comment comment:commentList){
                DetailComment detailComment=new DetailComment();
                detailComment.setCommentID(comment.getCommentID());
                detailComment.setCommentContent(comment.getCommentContent());
                detailComment.setCreateID(comment.getCreateID());
                detailComment.setCreateName(userService.findUserByUserID(comment.getCreateID()).getUserName());
                detailComment.setCommentTime(comment.getCommentTime());
                detailComment.setPicturePath(comment.getPicturePath());
                detailComment.setReviewNum(comment.getReviewNum());
                List<Review> reviewList= postService.getReviewListByCommentID(comment.getCommentID());
                List<DetailReview> detailReviewList=new ArrayList<>();
                for(Review review:reviewList){
                    DetailReview detailReview=new DetailReview();
                    detailReview.setReviewID(review.getReviewID());
                    detailReview.setReviewTime(review.getReviewTime());
                    detailReview.setReviewContent(review.getReviewContent());
                    detailReview.setReviewType(review.getReviewType());
                    detailReview.setFromID(review.getFromID());
                    detailReview.setFromName(userService.findUserByUserID(review.getFromID()).getUserName());
                    detailReview.setToID(review.getToID());
                    detailReview.setToName(userService.findUserByUserID(review.getToID()).getUserName());
                    detailReviewList.add(detailReview);
                }
                detailComment.setDetailReviewList(detailReviewList);
                detailCommentList.add(detailComment);
            }
            map.put("success",true);
            map.put("detailCommentList",detailCommentList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/postInfo")//已测试
    @ApiOperation("/帖子信息")
    @ApiImplicitParam(name = "postID",value = "帖子ID",required = true,dataType = "int")
    public Map<String,Object> postInfo(@RequestParam Integer postID){
        Map<String,Object> map=new HashMap<>();
        try {
            Post post= postService.getPostByPostID(postID);
            map.put("success",true);
            map.put("post",post);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }
}
