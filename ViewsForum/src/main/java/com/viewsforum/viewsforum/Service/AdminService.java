package com.viewsforum.viewsforum.Service;

import com.viewsforum.viewsforum.Entity.Admin;

public interface AdminService {
    // 根据用户ID查询是否是系统管理员
    Admin findSystemAdminByUserID(Integer userID);
}
