package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.*;
import com.viewsforum.viewsforum.Service.*;
import com.viewsforum.viewsforum.Utils.ParamChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(tags = "用户操作相关接口")
@RestController("/user")
public class UserController {
    private final ParamChecker paramChecker=new ParamChecker();

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private SystemMessageService systemMessageService;

    @Autowired
    private TopicService topicService;

    @Autowired PostService postService;



    @PostMapping("/register")
    @ApiOperation("用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名，5<=字符长度<=20", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码，6-10位，必须由数字或字母组成", required = true, dataType = "String"),
            @ApiImplicitParam(name = "rePassword", value = "重新确认密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱，遵循正则匹配：\n(^[a-zA-Z0-9]{1,10}@[a-zA-Z0-9]{1,5}\\.[a-zA-Z0-9]{1,5})", required = true, dataType = "String")
    })
    public Map<String,Object> register(@RequestParam String userName,@RequestParam String password,@RequestParam String rePassword,@RequestParam String email){
        Map<String, Object> map = new HashMap<>();
        try {
            // 格式检查
            if(!paramChecker.checkUserName(userName)){
                map.put("success",false);
                map.put("msg","用户名格式错误");
                return map;
            }
            if(!paramChecker.checkPassword(password)){
                map.put("success",false);
                map.put("msg","密码格式错误");
                return map;
            }
            if(!paramChecker.checkEmail(email)){
                map.put("success",false);
                map.put("msg","邮箱格式错误");
                return map;
            }
            // 逻辑
            if(userService.findUserByEmail(email)!=null){
                map.put("success",false);
                map.put("msg","邮箱已注册");
            }
            else if(userService.findUserByUserName(userName)!=null){
                map.put("success",false);
                map.put("msg","用户名已注册");
            }
            else if(!rePassword.equals(password)){
                map.put("success",false);
                map.put("msg","两次密码输入不一致");
            }
            else{
                User user=new User();
                user.setUserName(userName);
                user.setPassword(password);
                user.setEmail(email);
                user.setNote("这个人还没有留言");
                userService.addNewUser(user);
                log.info("");
                map.put("success",true);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value="用户名，5<=字符长度<=20",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value="用户密码，6-10位，必须由数字或字母组成",required = true,dataType = "String")
    })
    public Map<String,Object> login(HttpServletRequest request, @RequestParam String userName, @RequestParam String password){
        Map<String, Object> map = new HashMap<>();
        try {
            // 格式检查
            if(!paramChecker.checkUserName(userName)){
                map.put("success",false);
                map.put("msg","用户名格式错误");
                return map;
            }
            if(!paramChecker.checkPassword(password)){
                map.put("success",false);
                map.put("msg","密码格式错误");
                return map;
            }

            User user=userService.findUserByUserNameAndPassword(userName,password);
            if (userService.findUserByUserName(userName)==null) {
                map.put("success", false);
                map.put("msg", "用户不存在");
            }
            else if(user==null){
                map.put("success",false);
                map.put("msg","密码错误");
            }
            else{
                boolean isAdmin = (adminService.findSystemAdminByUserID(user.getUserID())!=null);
                HttpSession session=request.getSession();
                session.setAttribute("userID",user.getUserID());
                map.put("success", true);
                map.put("user",user);
                map.put("isAdmin",isAdmin);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            map.put("success", false);
            map.put("msg", "INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/forgetPassword")
    @ApiOperation("忘记密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value="用户名，5<=字符长度<=20",required = true,dataType = "String"),
            @ApiImplicitParam(name = "email，遵循正则匹配：\n(^[a-zA-Z0-9]{1,10}@[a-zA-Z0-9]{1,5}\\.[a-zA-Z0-9]{1,5})",value="邮箱",required = true,dataType = "String")
    })
    public Map<String,Object> forgetPassword(HttpServletRequest request,@RequestParam String userName, @RequestParam String email){
        Map<String,Object> map=new HashMap<>();
        try{
            // 格式检查
            if(!paramChecker.checkUserName(userName)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(!paramChecker.checkEmail(email)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }

            User user = userService.findUserByUserNameAndEmail(userName,email);
            if(userService.findUserByUserName(userName)==null){
                map.put("success", false);
                map.put("msg", "用户名不存在");
            }
            else if(user==null){
                map.put("success", false);
                map.put("msg", "用户邮箱不正确");
            }
            else{
                HttpSession session=request.getSession();
                emailService.sendEmail(email,session);
                map.put("success", true);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success", false);
            map.put("msg", "INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/verify")
    @ApiOperation("验证验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName",value="用户名，5<=字符长度<=20",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value="密码，6-10位，必须由数字或字母组成",required = true,dataType = "String"),
            @ApiImplicitParam(name = "rePassword",value = "重新确认密码，6-10位，必须由数字或字母组成",required = true,dataType = "String"),
            @ApiImplicitParam(name = "code",value = "验证码",required = true,dataType = "String")
    })
    public Map<String,Object> resetPassword(HttpServletRequest request,@RequestParam String userName,@RequestParam String password,@RequestParam String rePassword,@RequestParam String code){
        Map<String,Object> map=new HashMap<>();
        try{
            // 格式检查
            if(!paramChecker.checkPassword(password)||!paramChecker.checkNotNull(rePassword)||!paramChecker.checkNotNull(code)){
                map.put("success",false);
                map.put("msg","密码格式错误");
                return map;
            }

            if(!rePassword.equals(password)){
                map.put("success",false);
                map.put("msg","两次密码输入不一致");
            }
            else if(request.getSession().getAttribute("code")==null){
                map.put("success",false);
                map.put("msg","验证码过期");
            }
            else if(!request.getSession().getAttribute("code").equals(code)){
                map.put("success",false);
                map.put("msg","验证码错误");
            }
            else{
                userService.changePasswordByUserIDAndPassword(userName, DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
                request.getSession().removeAttribute("code");
                map.put("success",true);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/logout")
    @ApiOperation("注销接口")
    public Map<String,Object> logout (HttpServletRequest request){
        Map<String ,Object> map=new HashMap<>();
        try {
            HttpSession session=request.getSession();
            session.removeAttribute("userID");
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/followUser")
    @ApiOperation("关注用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "followerID",value = "关注者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "followedID",value = "被关注者ID",required = true,dataType = "int")
    })
    public Map<String,Object> followUser(@RequestParam Integer followerID,@RequestParam Integer followedID){
        Map<String,Object> map=new HashMap<>();
        try {
            // 检查是否关注
            if(userService.findUserFollowByFollowerIDAndFollowedID(followerID,followedID)!=null){
                map.put("success",false);
                map.put("msg","已关注");
                return map;
            }
            // 添加关注
            User follower = userService.findUserByUserID(followerID);
            User followed = userService.findUserByUserID(followedID);
            UserFollow userFollow = new UserFollow();
            userFollow.setFollowerID(followerID);
            userFollow.setFollowedID(followedID);
            userFollow.setFollowTime(new Timestamp(System.currentTimeMillis()));
            userService.followUser(userFollow);

            // 发送系统消息
            SystemMessage systemMessage=new SystemMessage();
            systemMessage.setUserID(followedID);
            systemMessage.setMessageType(3);
            systemMessage.setMessageTime(new Timestamp(System.currentTimeMillis()));
            systemMessage.setMessageContent(follower.getUserName()+" 关注了您");
            systemMessageService.addNewSystemMessage(systemMessage);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/unFollowUser")
    @ApiOperation("取消关注用户")
    @ApiImplicitParam(name = "userFollowID",value = "用户关注ID",required = true,dataType = "int")
    public Map<String,Object> unFollowUser(@RequestParam Integer userFollowID){
        Map<String,Object> map=new HashMap<>();
        try {
            userService.unFollowUser(userFollowID);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/blackoutUser")
    @ApiOperation("拉黑用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "blackerID",value = "拉黑者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "blackedID",value = "被拉黑者ID",required = true,dataType = "int")
    })
    public Map<String,Object> blackoutUser(@RequestParam Integer blackerID,@RequestParam Integer blackedID){
        Map<String,Object> map=new HashMap<>();
        try {
            if(userService.findBlackByBlackerIDAndBlackedID(blackerID,blackedID)!=null){
                map.put("success",false);
                map.put("msg","已拉黑");
                return map;
            }
            Black black=new Black();
            black.setBlackerID(blackerID);
            black.setBlackedID(blackedID);
            black.setBlackedTime(new Timestamp(System.currentTimeMillis()));
            userService.blackoutUser(black);

            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/unBlackoutUser")
    @ApiOperation("取消拉黑用户")
    @ApiImplicitParam(name = "blackID",value = "黑名单ID",required = true,dataType = "int")
    public Map<String,Object> unBlackoutUser(@RequestParam Integer blackID){
        Map<String,Object> map=new HashMap<>();
        try {
            userService.unBlackoutUser(blackID);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/isFollowUser")
    @ApiOperation("查询是否已关注用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "followerID",value = "关注者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "followedID",value = "被关注者ID",required = true,dataType = "int")
    })
    public Map<String,Object> isFollowUser(@RequestParam Integer followerID,@RequestParam Integer followedID){
        Map<String,Object> map=new HashMap<>();
        try {
            boolean isFollow=userService.findUserFollowByFollowerIDAndFollowedID(followerID,followedID)!=null;
            map.put("success",true);
            map.put("isFollow",isFollow);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/isBlackoutUser")
    @ApiOperation("查询是否已拉黑用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "blackerID",value = "拉黑者ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "blackedID",value = "被拉黑者ID",required = true,dataType = "int")
    })
    public Map<String,Object> isBlackoutUser(@RequestParam Integer blackerID,@RequestParam Integer blackedID){
        Map<String,Object> map=new HashMap<>();
        try {
            boolean isBlackout=userService.findBlackByBlackerIDAndBlackedID(blackerID,blackedID)!=null;
            map.put("success",true);
            map.put("isBlackout",isBlackout);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/getUserFollowList")
    @ApiOperation("关注用户列表")
    @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int")
    public Map<String,Object> getUserFollowList(@RequestParam Integer userID){
        Map<String,Object> map=new HashMap<>();
        try {
            List<UserFollow> userFollowList=userService.getUserFollowList(userID);
            List<User> userList=new ArrayList<>();
            if(userFollowList!=null&&userFollowList.size()!=0){
                for(UserFollow userFollow:userFollowList){
                    userList.add(userService.findUserByUserID(userFollow.getFollowedID()));
                }
            }
            map.put("success",true);
            map.put("userFollowList",userFollowList);
            map.put("userList",userList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/getBlackList")
    @ApiOperation("拉黑用户列表")
    @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int")
    public Map<String,Object> getBlackList(@RequestParam Integer userID){
        Map<String,Object> map=new HashMap<>();
        try {
            List<Black> blackList=userService.getBlackList(userID);
            List<User> userList=new ArrayList<>();
            if(blackList!=null&&blackList.size()!=0){
                for(Black black:blackList){
                    userList.add(userService.findUserByUserID(black.getBlackedID()));
                }
            }
            map.put("success",true);
            map.put("blackList",blackList);
            map.put("userList",userList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/getTopicFollowList")
    @ApiOperation("关注主题列表")
    @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int")
    public Map<String,Object> getTopicFollowList(@RequestParam Integer userID){
        Map<String,Object> map=new HashMap<>();
        try {
            List<TopicFollow> topicFollowList=userService.getTopicFollowList(userID);
            List<Topic> topicList=new ArrayList<>();
            if(topicFollowList!=null&&topicFollowList.size()!=0){
                for(TopicFollow topicFollow:topicFollowList){
                    topicList.add(topicService.findTopicByTopicID(topicFollow.getTopicID()));
                }
            }
            map.put("success",true);
            map.put("topicFollowList",topicFollowList);
            map.put("topicList",topicList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/getMyTopic")
    @ApiOperation("发布的主题列表")
    @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int")
    public Map<String,Object> getMyTopic(@RequestParam Integer userID){
        Map<String,Object> map=new HashMap<>();
        try {
            List<Topic> topicList=topicService.findCreateTopicListByCreateID(userID);
            map.put("success",true);
            map.put("topicList",topicList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @GetMapping("/getMyPost")
    @ApiOperation("发布的帖子列表")
    @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "int")
    public Map<String,Object> getMyPost(@RequestParam Integer userID){
        Map<String,Object> map=new HashMap<>();
        try {
            List<Post> postList=postService.getCreatePostListByCreateID(userID);
            map.put("success",true);
            map.put("postList",postList);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    @PostMapping("/editUserInfo")
    @ApiOperation("修改个人信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userID",value = "用户ID",required = true,dataType = "String"),
            @ApiImplicitParam(name = "userName",value = "用户名，5<=字符长度<=20",required = true,dataType = "String"),
            @ApiImplicitParam(name = "email",value = "邮箱，遵循正则匹配：\n(^[a-zA-Z0-9]{1,10}@[a-zA-Z0-9]{1,5}\\.[a-zA-Z0-9]{1,5})",required = true,dataType = "String"),
            @ApiImplicitParam(name = "note",value = "备注，1<=字符长度<=100",required = true,dataType = "String")
    })
    public Map<String,Object> editUserInfo(@RequestParam Integer userID,@RequestParam String userName,@RequestParam String email,@RequestParam String note){
        Map<String,Object> map=new HashMap<>();
        try {
            if(!paramChecker.checkUserName(userName)){
                map.put("success",false);
                map.put("msg","用户名格式错误");
                return map;
            }
            if(!paramChecker.checkEmail(email)){
                map.put("success",false);
                map.put("msg","邮箱格式错误");
                return map;
            }
            if(!paramChecker.checkNote(note)){
                map.put("success",false);
                map.put("msg","备注格式错误");
                return map;
            }

            User originalUser=userService.findUserByUserID(userID);
            User nameUser=userService.findUserByUserName(userName);
            User emailUser=userService.findUserByEmail(email);
            // 检查用户名、邮箱是否被占用
            if(nameUser!=null&& !nameUser.getUserID().equals(userID)){
                map.put("success",false);
                map.put("msg","用户名已被占用");
                return map;
            }
            if(emailUser!=null&& !emailUser.getUserID().equals(userID)){
                map.put("success",false);
                map.put("msg","邮箱已被占用");
                return map;
            }
            // 修改备注
            userService.editUserInfoByUserID(userID,userName,email,note);
            map.put("success",true);
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",true);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }
}
