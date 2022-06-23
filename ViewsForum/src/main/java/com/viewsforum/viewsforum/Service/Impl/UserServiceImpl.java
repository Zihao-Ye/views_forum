package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.UserDao;
import com.viewsforum.viewsforum.Entity.Black;
import com.viewsforum.viewsforum.Entity.TopicFollow;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Entity.UserFollow;
import com.viewsforum.viewsforum.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void changePasswordByUserIDAndPassword(String userName, String password){
        userDao.changePasswordByUserIDAndPassword(userName,password);
    }

    // 根据用户名与邮箱查询用户
    public User findUserByUserNameAndEmail(String userName,String email){
        return userDao.findUserByUserNameAndEmail(userName,email);
    }

    // 根据用户ID查询用户
    public User findUserByUserID(Integer userID){
        return userDao.findUserByUserID(userID);
    }

    // 根据关注者ID与被关注者ID查询关注情况
    public UserFollow findUserFollowByFollowerIDAndFollowedID(Integer followerID, Integer followedID){
        return userDao.findUserFollowByFollowerIDAndFollowedID(followerID,followedID);
    }

    // 添加关注
    public void followUser(UserFollow userFollow){
        userDao.followUser(userFollow);
    }

    // 取消关注用户
    public void unFollowUser(Integer userFollowID){
        userDao.unFollowUser(userFollowID);
    }

    // 根据拉黑者ID与被拉黑者ID查询拉黑情况
    public Black findBlackByBlackerIDAndBlackedID(Integer blackerID, Integer blackedID){
        return userDao.findBlackByBlackerIDAndBlackedID(blackerID,blackedID);
    }

    // 拉黑用户
    public void blackoutUser(Black black){
        userDao.blackoutUser(black);
    }

    // 取消拉黑用户
    public void unBlackoutUser(Integer blackID){
        userDao.unBlackoutUser(blackID);
    }

    // 获取关注用户列表
    public List<UserFollow> getUserFollowList(Integer followerID){
        return userDao.getUserFollowList(followerID);
    }

    // 获取拉黑用户列表
    public List<Black> getBlackList(Integer blackerID){
        return userDao.getBlackList(blackerID);
    }

    // 获取关注主题列表
    public List<TopicFollow> getTopicFollowList(Integer followerID){
        return userDao.getTopicFollowList(followerID);
    }

    // 修改用户信息
    public void editUserInfoByUserID(Integer userID,String userName,String email,String note){
        userDao.editUserInfoByUserID(userID,userName,email,note);
    }

    // 根据用户ID取关
    public void unFollowUserByFollowedID(Integer followerID, Integer followedID){
        userDao.unFollowUserByFollowedID(followerID,followedID);
    }
}
