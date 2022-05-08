package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.Admin;
import com.viewsforum.viewsforum.Entity.AdminApply;
import com.viewsforum.viewsforum.Entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminDao {
    // 根据用户ID查询是否是系统管理员
    Admin findSystemAdminByUserID(Integer userID);

    // 发送申请
    void addNewAdminApply(AdminApply adminApply);

    // 根据用户ID与主题ID查询是否已申请主题管理员
    AdminApply findApplyByUserIDAndTopicID(Integer userID,Integer topicID);

    // 根据用户ID与主题ID查询是否是主题管理员
    Admin findTopicAdminByUserIDAndTopicID(Integer userID,Integer topicID);

    // 根据主题ID查询按时间顺序查询管理员申请
    List<AdminApply> findAllAdminApplyByTopicID(Integer topicID);

    // 根据用户ID与主题ID查询是否是主题创建者
    Admin findTopicCreatorByUserIDAndTopicID(Integer userID,Integer topicID);

    // 根据申请ID查询申请
    AdminApply findApplyByApplyID(Integer applyID);

    // 根据申请ID设置申请已处理
    void finishAdminApplyByApplyID(Integer applyID);

    // 添加管理员
    void addNewTopicAdmin(Admin admin);

    // 根据主题ID获取管理员
    List<Admin> findAllAdminByTopicID(Integer topicID);

    // 根据管理员ID查询管理员
    Admin findAdminByAdminID(Integer adminID);

    // 根据管理员ID取消管理员身份
    void deleteAdmin(Integer adminID);
}
