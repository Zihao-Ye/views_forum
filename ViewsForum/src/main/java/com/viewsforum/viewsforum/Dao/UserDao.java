package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    // 根据邮箱查询用户
    User findUserByEmail(String email);

    // 根据用户名查询用户
    User findUserByUserName(String userName);

    // 添加用户
    void addNewUser(User user);

    // 根据用户名与密码查询用户
    User findUserByUserNameAndPassword(String userName,String password);

    // 根据用户ID与密码修改密码
    void changePasswordByUserIDAndPassword(Integer userID,String password);

    // 根据用户名与邮箱查询用户
    User findUserByUserNameAndEmail(String userName,String email);

    // 根据用户ID查询用户
    User findUserByUserID(Integer userID);
}
