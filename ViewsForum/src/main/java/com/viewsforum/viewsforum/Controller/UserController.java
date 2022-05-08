package com.viewsforum.viewsforum.Controller;

import com.viewsforum.viewsforum.Entity.AdminApply;
import com.viewsforum.viewsforum.Entity.SystemMessage;
import com.viewsforum.viewsforum.Entity.Topic;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Service.AdminService;
import com.viewsforum.viewsforum.Service.EmailService;
import com.viewsforum.viewsforum.Service.SystemMessageService;
import com.viewsforum.viewsforum.Service.UserService;
import com.viewsforum.viewsforum.Utils.ParamChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api("用户操作相关接口")
@RestController("/user")
public class UserController {
    private final ParamChecker paramChecker=new ParamChecker();

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;



    @PostMapping("/register")
    @ApiOperation("用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "rePassword", value = "重新确认密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String")
    })
    public Map<String,Object> register(@RequestParam String userName,@RequestParam String password,@RequestParam String rePassword,@RequestParam String email){
        Map<String, Object> map = new HashMap<>();
        try {
            // 格式检查
            if(!paramChecker.checkUserName(userName)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(!paramChecker.checkPassword(password)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(!paramChecker.checkEmail(email)){
                map.put("success",false);
                map.put("msg","格式错误");
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
                userService.addNewUser(user);
                log.info("");
                map.put("success",true);
                map.put("message","用户注册成功");
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
            @ApiImplicitParam(name = "userName",value="用户名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "password",value="用户密码",required = true,dataType = "String")
    })
    public Map<String,Object> login(HttpServletRequest request, @RequestParam String userName, @RequestParam String password){
        Map<String, Object> map = new HashMap<>();
        try {
            // 格式检查
            if(!paramChecker.checkUserName(userName)){
                map.put("success",false);
                map.put("msg","格式错误");
                return map;
            }
            if(!paramChecker.checkPassword(password)){
                map.put("success",false);
                map.put("msg","格式错误");
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
                map.put("msg", "用户登录成功");
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
            @ApiImplicitParam(name = "userName",value="用户名",required = true,dataType = "String"),
            @ApiImplicitParam(name = "email",value="邮箱",required = true,dataType = "String")
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
                map.put("msg", "邮件已发送，请查看你绑定的邮箱");
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
            @ApiImplicitParam(name = "userID",value="用户ID",required = true,dataType = "int"),
            @ApiImplicitParam(name = "password",value="密码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "rePassword",value = "重新确认密码",required = true,dataType = "String"),
            @ApiImplicitParam(name = "code",value = "验证码",required = true,dataType = "String")
    })
    public Map<String,Object> resetPassword(HttpServletRequest request,@RequestParam Integer userID,@RequestParam String password,@RequestParam String rePassword,@RequestParam String code){
        Map<String,Object> map=new HashMap<>();
        try{
            // 格式检查
            if(!paramChecker.checkPassword(password)||!paramChecker.checkNotNull(rePassword)||!paramChecker.checkNotNull(code)){
                map.put("success",false);
                map.put("msg","格式错误");
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
                userService.changePasswordByUserIDAndPassword(userID, DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
                request.getSession().removeAttribute("code");
                map.put("success",true);
                map.put("msg","修改密码成功");
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
            map.put("msg","注销成功");
        }catch (Exception e){
            log.error(e.getMessage());
            map.put("success",false);
            map.put("msg","INTERNAL_ERROR");
        }
        return map;
    }

    //todo 修改个人信息

    //todo 关注

    //todo 取消关注

    //todo 拉黑

    //todo 取消拉黑

    //todo 关注的主题

    //todo 发布的主题

    //todo 发布的帖子
}
