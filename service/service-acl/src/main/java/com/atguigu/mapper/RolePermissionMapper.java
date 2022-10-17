package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-11  09:17
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    List<Long> findPermissionIdListByRoleId(Long roleId);

    void deleteByRoleId(Long roleId);

    void deletByRoleIdAndPermissionIdList(@Param("roleId") Long roleId,@Param("removePermissionIdList") List<Long> removePermissionIdList);

    RolePermission findByRoleIdAndPermissionId(@Param("roleId") Long roleId,@Param("permissionId") Long permissionId);
}
