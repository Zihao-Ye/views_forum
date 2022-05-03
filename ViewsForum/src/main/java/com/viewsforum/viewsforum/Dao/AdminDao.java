package com.viewsforum.viewsforum.Dao;

import com.viewsforum.viewsforum.Entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminDao {
    // 根据用户ID查询是否是系统管理员
    Admin findSystemAdminByUserID(Integer userID);
}
