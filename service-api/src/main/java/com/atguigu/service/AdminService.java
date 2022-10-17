package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-09-30  15:06
 */
public interface AdminService extends BaseService<Admin> {
    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    Admin getByUsername(String username);
}
