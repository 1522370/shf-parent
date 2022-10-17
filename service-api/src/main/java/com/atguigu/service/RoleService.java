package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-09-29  11:45
 */
public interface RoleService extends BaseService<Role> {
    /**
     * 根据用户id，查询用户已分配和未分配的角色列表
     * @param adminId
     * @return
     */
    Map<String, List<Role>> findRoleListMapByAdminId(Long adminId);

    /**
     * 保存给用户分配角色
     * @param adminId
     * @param roleIds
     */
    void saveAdminRole(Long adminId, List<Long> roleIds);

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> findByAdminId(Long adminId);
}
