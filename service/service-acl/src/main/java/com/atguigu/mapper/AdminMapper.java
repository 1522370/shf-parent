package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-09-30  15:01
 */
public interface AdminMapper extends BaseMapper<Admin> {
    Admin getByUsername(String username);
}
