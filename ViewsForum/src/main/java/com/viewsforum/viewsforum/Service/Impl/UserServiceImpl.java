package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.UserDao;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    // 根据邮箱查询用户
    public User findUserByEmail(String email){
        return userDao.findUserByEmail(email);
    }

    // 根据用户名查询用户
    public User findUserByUserName(String userName){
        return userDao.findUserByUserName(userName);
    }

    // 添加用户
    public void addNewUser(User user){
        userDao.addNewUser(user);
    }

    // 根据用户名与密码查询用户
    public User findUserByUserNameAndPassword(String userName,String password){
        return userDao.findUserByUserNameAndPassword(userName,password);
    }

    // 根据用户ID与密码修改密码
    public void changePasswordByUserIDAndPassword(Integer userID, String password){
        userDao.changePasswordByUserIDAndPassword(userID,password);
    }

    // 根据用户名与邮箱查询用户
    public User findUserByUserNameAndEmail(String userName,String email){
        return userDao.findUserByUserNameAndEmail(userName,email);
    }

    // 根据用户ID查询用户
    public User findUserByUserID(Integer userID){
        return userDao.findUserByUserID(userID);
    }
}
