package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Role;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-09-29  11:11
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> findByAdminId(Long adminId);
}
