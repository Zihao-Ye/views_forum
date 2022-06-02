package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.*;
import com.viewsforum.viewsforum.Pojo.AdminControllerPojo.AdminApplyUser;
import com.viewsforum.viewsforum.Pojo.AdminControllerPojo.AdminUser;
import com.viewsforum.viewsforum.Pojo.PostControllerPojo.DetailComment;
import com.viewsforum.viewsforum.Pojo.PostControllerPojo.DetailReview;
import com.viewsforum.viewsforum.Pojo.UserControllerPojo.BlackUser;
import com.viewsforum.viewsforum.Pojo.UserControllerPojo.TopicFollowTopic;
import com.viewsforum.viewsforum.Pojo.UserControllerPojo.UserFollowUser;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "实体类", description = "请不要在此接口上进行测试，本接口仅用于提供实体类信息")
@RestController("/modelInfo")
public class ModelController {

    @GetMapping("/admin")
    public Admin admin(){ return new Admin(); }

    @GetMapping("/adminApply")
    public AdminApply adminApply(){ return new AdminApply(); }

    @GetMapping("/black")
    public Black black(){ return new Black(); }

    @GetMapping("/chat")
    public Chat chat(){ return new Chat(); }

    @GetMapping("/comment")
    public Comment comment(){ return new Comment(); }

    @GetMapping("post")
    public Post post(){ return new Post(); }

    @GetMapping("/review")
    public Review review(){ return new Review(); }

    @GetMapping("/systemMessage")
    public SystemMessage systemMessage(){ return new SystemMessage(); }

    @GetMapping("/topic")
    public Topic topic(){ return new Topic(); }

    @GetMapping("/topicFollow")
    public TopicFollow topicFollow(){ return new TopicFollow(); }

    @GetMapping("/user")
    public User user(){ return new User(); }

    @GetMapping("/userFollow")
    public UserFollow userFollow(){ return new UserFollow(); }
    
    @GetMapping("/adminApplyUser")
    public AdminApplyUser adminApplyUser(){ return new AdminApplyUser(); }

    @GetMapping("/adminUser")
    public AdminUser adminUser(){ return new AdminUser(); }

    @GetMapping("/detailComment")
    public DetailComment detailComment(){ return new DetailComment(); }

    @GetMapping("/detailReview")
    public DetailReview detailReview(){ return new DetailReview(); }

    @GetMapping("/blackUser")
    public BlackUser blackUser(){ return new BlackUser(); }

    @GetMapping("/topicFollowTopic")
    public TopicFollowTopic topicFollowTopic(){ return new TopicFollowTopic(); }

    @GetMapping("/userFollowUser")
    public UserFollowUser userFollowUser(){ return new UserFollowUser(); }
}
