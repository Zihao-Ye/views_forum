package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender javaMailSender;
    @Override
    public void sendEmail(String Email, HttpSession session){
        String code=randomCode();
        session.setAttribute("code",code);
        try{
            SimpleMailMessage message=new SimpleMailMessage();
            message.setSubject("找回密码邮件");
            message.setFrom("1160411003@qq.com");
            message.setTo(Email);
            message.setText("您好:\n"
                    + "\n本次请求的邮件验证码为:" + code + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                    + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");
            javaMailSender.send(message);
            session.setMaxInactiveInterval(5*60);
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    private String randomCode(){
        String SYMBOLS = "0123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
        Random RANDOM = new SecureRandom();
        char[] numbers = new char[6];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(numbers);
    }
}
