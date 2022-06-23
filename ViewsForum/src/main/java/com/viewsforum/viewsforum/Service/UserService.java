package com.viewsforum.viewsforum.Service;

import com.viewsforum.viewsforum.Entity.Black;
import com.viewsforum.viewsforum.Entity.TopicFollow;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Entity.UserFollow;

import java.util.List;

public interface UserService {
    // 根据邮箱查询用户
    User findUserByEmail(String email);

    // 根据用户名查询用户
    User findUserByUserName(String userName);

    // 添加用户
    void addNewUser(User user);

    // 根据用户名与密码查询用户
    User findUserByUserNameAndPassword(String userName,String password);

    // 根据用户ID与密码修改密码
    void changePasswordByUserIDAndPassword(String userName, String password);

    // 根据用户名与邮箱查询用户
    User findUserByUserNameAndEmail(String userName,String email);

    // 根据用户ID查询用户
    User findUserByUserID(Integer userID);

    // 根据关注者ID与被关注者ID查询关注情况
    UserFollow findUserFollowByFollowerIDAndFollowedID(Integer followerID,Integer followedID);

    // 添加关注
    void followUser(UserFollow userFollow);

    // 取消关注用户
    void unFollowUser(Integer userFollowID);

    // 根据拉黑者ID与被拉黑者ID查询拉黑情况
    Black findBlackByBlackerIDAndBlackedID(Integer blackerID,Integer blackedID);

    // 拉黑用户
    void blackoutUser(Black black);

    // 取消拉黑用户
    void unBlackoutUser(Integer blackID);

    // 获取关注用户列表
    List<UserFollow> getUserFollowList(Integer followerID);

    // 获取拉黑用户列表
    List<Black> getBlackList(Integer blackerID);

    // 获取关注主题列表
    List<TopicFollow> getTopicFollowList(Integer followerID);

    // 修改用户信息
    void editUserInfoByUserID(Integer userID,String userName,String email,String note);

    // 根据用户ID取关
    void unFollowUserByFollowedID(Integer followerID, Integer followedID);
}
