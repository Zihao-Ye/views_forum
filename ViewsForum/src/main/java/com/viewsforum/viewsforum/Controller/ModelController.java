package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.*;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "实体类", description = "请不要在此接口上进行测试，本接口仅用于提供实体类信息")
@RestController("/modelInfo")
public class ModelController {

    @PostMapping("/admin")
    public Admin admin(){ return new Admin(); }

    @PostMapping("/adminApply")
    public AdminApply adminApply(){ return new AdminApply(); }

    @PostMapping("/black")
    public Black black(){ return new Black(); }

    @PostMapping("/chat")
    public Chat chat(){ return new Chat(); }

    @PostMapping("/comment")
    public Comment comment(){ return new Comment(); }

    @PostMapping("/picture")
    public Picture picture(){ return new Picture(); }

    @PostMapping("post")
    public Post post(){ return new Post(); }

    @PostMapping("/review")
    public Review review(){ return new Review(); }

    @PostMapping("/systemMessage")
    public SystemMessage systemMessage(){ return new SystemMessage(); }

    @PostMapping("/topic")
    public Topic topic(){ return new Topic(); }

    @PostMapping("/topicFollow")
    public TopicFollow topicFollow(){ return new TopicFollow(); }

    @PostMapping("/user")
    public User user(){ return new User(); }

    @PostMapping("/userFollow")
    public UserFollow userFollow(){ return new UserFollow(); }
}
