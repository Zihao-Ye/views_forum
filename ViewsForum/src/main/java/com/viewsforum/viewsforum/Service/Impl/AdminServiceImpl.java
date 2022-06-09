package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.AdminDao;
import com.viewsforum.viewsforum.Entity.Admin;
import com.viewsforum.viewsforum.Entity.AdminApply;
import com.viewsforum.viewsforum.Entity.User;
import com.viewsforum.viewsforum.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;

    // 根据用户ID查询是否是系统管理员
    public Admin findSystemAdminByUserID(Integer userID){
        return adminDao.findSystemAdminByUserID(userID);
    }

    // 发送申请
    public void addNewAdminApply(AdminApply adminApply){
        adminDao.addNewAdminApply(adminApply);
    }

    // 根据用户ID与主题ID查询是否已申请主题管理员
    public AdminApply findApplyByUserIDAndTopicID(Integer userID,Integer topicID){
        return adminDao.findApplyByUserIDAndTopicID(userID,topicID);
    }

    // 根据用户ID与主题ID查询是否是主题管理员
    public Admin findTopicAdminByUserIDAndTopicID(Integer userID,Integer topicID){
        return adminDao.findTopicAdminByUserIDAndTopicID(userID,topicID);
    }

    // 根据主题ID查询按时间顺序查询管理员申请
    public List<AdminApply> findAllAdminApplyByTopicID(Integer topicID){
        return adminDao.findAllAdminApplyByTopicID(topicID);
    }

    // 根据用户ID与主题ID查询是否是主题创建者
    public Admin findTopicCreatorByUserIDAndTopicID(Integer userID,Integer topicID){
        return adminDao.findTopicCreatorByUserIDAndTopicID(userID,topicID);
    }

    // 根据申请ID查询申请
    public AdminApply findApplyByApplyID(Integer applyID){
        return adminDao.findApplyByApplyID(applyID);
    }

    // 根据申请ID设置申请已处理
    public void finishAdminApplyByApplyID(Integer applyID){
        adminDao.finishAdminApplyByApplyID(applyID);
    }

    // 添加管理员
    public void addNewTopicAdmin(Admin admin){
        adminDao.addNewTopicAdmin(admin);
    }

    // 根据主题ID获取管理员
    public List<Admin> findAllAdminByTopicID(Integer topicID){
        return adminDao.findAllAdminByTopicID(topicID);
    }

    // 根据管理员ID查询管理员
    public Admin findAdminByAdminID(Integer adminID){
        return adminDao.findAdminByAdminID(adminID);
    }

    // 根据管理员ID取消管理员身份
    public void deleteAdmin(Integer adminID){
        adminDao.deleteAdmin(adminID);
    }

    // 根据主题ID删除主题
    public void deleteTopic(Integer topicID){
        adminDao.deleteTopic(topicID);
    }

    // 根据帖子ID删除帖子
    public void deletePost(Integer postID){
        adminDao.deletePost(postID);
    }

    // 根据评论ID删除评论
    public void deleteComment(Integer commentID){
        adminDao.deleteComment(commentID);
    }

    // 根据回复ID删除回复
    public void deleteReview(Integer reviewID){
        adminDao.deleteReview(reviewID);
    }
}
