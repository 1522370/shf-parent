package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-10-11  09:05
 */
public interface PermissionService extends BaseService<Permission> {
    List<Map<String, Object>> findPermissionByRoleId(Long roleId);

    /**
     * 给角色分配权限
     * @param roleId
     * @param permissionIds
     */
    void saveRolePermission(Long roleId, List<Long> permissionIds);

    /**
     * 根据用户id查询用户的动态菜单
     * @param adminId
     * @return
     */
    List<Permission> findMenuPermissionByAdminId(Long adminId);

    /**
     * 根据用户id查询用户的三级菜单的code列表
     * @param adminId
     * @return
     */
    List<String> findPermissionCodeListByAdminId(Long adminId);
}
