package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Permission;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-11  09:06
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> findByParentId(Long parentId);

    /**
     * 根据用户id查询权限列表
     * @param adminId
     * @return
     */
    List<Permission> findPermissionListByAdminId(Long adminId);
}
