package com.viewsforum.viewsforum.Service.Impl;

import com.viewsforum.viewsforum.Dao.AdminDao;
import com.viewsforum.viewsforum.Entity.Admin;
import com.viewsforum.viewsforum.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminDao adminDao;

    // 根据用户ID查询是否是系统管理员
    public Admin findSystemAdminByUserID(Integer userID){
        return adminDao.findSystemAdminByUserID(userID);
    }
}
