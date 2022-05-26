package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.Comment;
import com.viewsforum.viewsforum.Entity.Review;
import com.viewsforum.viewsforum.Entity.SystemMessage;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Service.PostService;
import com.viewsforum.viewsforum.Service.SystemMessageService;
import com.viewsforum.viewsforum.Service.UserService;
import com.viewsforum.viewsforum.Utils.ParamChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

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
    private SystemMessageService systemMessageService;


    //todo 创建帖子

    //todo 获取评论


    //todo 发表评论

    @PostMapping("/reviewToCommentOrReview")
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
            if(!paramChecker.checkReview(reviewContent)){
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
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }
}
