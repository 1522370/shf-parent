package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-10  14:26
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    List<Long> findRoleIdListByAdminId(Long adminId);

    void deleteByAdminId(Long adminId);

    void deleteByAdminIdAndRoleIdList(@Param("adminId") Long adminId,@Param("removeRoleIdList") List<Long> removeRoleIdList);

    AdminRole findByAdminIdAndRoleId(@Param("adminId") Long adminId,@Param("roleId") Long roleId);
}
