package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.entity.RolePermission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-10-11  09:06
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public BaseMapper<Permission> getEntityMapper() {
        return permissionMapper;
    }

    @Override
    public List<Map<String, Object>> findPermissionByRoleId(Long roleId) {
        //1. 查询出所有权限
        List<Permission> allPermissionList = permissionMapper.findAll();
        //2. 每一个Permission应该映射成一个Map，Map中包含五个键值对
        //声明一个集合用来存储Permission
        List<Map<String,Object>> zNodes = new ArrayList<>();
        //查询当前角色已分配的权限id集合
        List<Long> assignPermissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        for (Permission permission : allPermissionList) {
            //2.1 创建Map
            Map<String,Object> zNode = new HashMap<>();
            //2.2 往Map中设置五个键值对
            zNode.put("id",permission.getId());
            zNode.put("pId",permission.getParentId());
            zNode.put("name",permission.getName());
            //设置open属性，那么我们要判断当前permission是否还有子节点，如果有则设置open属性为true，否则不设置
            //怎么判断当前节点是否还有子节点: 以当前节点的id作为parent_id查询List<Permission>，如果查询到的集合不为空，说明当前节点还有子节点
            if (!CollectionUtils.isEmpty(permissionMapper.findByParentId(permission.getId()))) {
                zNode.put("open",true);
            }
            //设置checked属性,那么我们要判断当前permission是否分配给了当前角色,如果分配了就设置checked为true，否则为false
            //怎么判断当前permission是否分配给了当前角色
            zNode.put("checked",assignPermissionIdList.contains(permission.getId()));

            //将zNode添加到zNodes里面去
            zNodes.add(zNode);
        }
        return zNodes;
    }

    //简单做法
    /*@Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {
        //1. 删除当前角色已分配的所有权限
        rolePermissionMapper.deleteByRoleId(roleId);

        //2. 新增分配权限
        if (!CollectionUtils.isEmpty(permissionIds)) {
            for (Long permissionId : permissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId(permissionId);

                rolePermissionMapper.insert(rolePermission);
            }
        }
    }*/

    //复杂做法
    @Override
    public void saveRolePermission(Long roleId, List<Long> permissionIds) {
        //一、 找出需要删除的，进行删除
        //1. 找出之前已分配给该角色的所有权限
        List<Long> assignPermissionIdList = rolePermissionMapper.findPermissionIdListByRoleId(roleId);
        //2. 创建一个集合用来存储需要删除的分配信息中的权限id
        List<Long> removePermissionIdList = new ArrayList<>();
        //43. 判断permissionIds是否为空(判断这次需不需要分配权限)
        if (!CollectionUtils.isEmpty(permissionIds)) {
            //3.2 如果这次需要分配权限
            //3.3.1 继续判断之前有没有分配过权限
            if (!CollectionUtils.isEmpty(assignPermissionIdList)) {
                //3.3.1.1 如果之前分配过权限:  之前分配了，这次不需要分配了的权限就是要删除的
                for (Long assignPermissionId : assignPermissionIdList) {
                    if (!permissionIds.contains(assignPermissionId)) {
                        removePermissionIdList.add(assignPermissionId);
                    }
                }
            }
        }else {
            //3.1 如果这次不需要分配任何权限 -----> 将之前已分配的全删除
            removePermissionIdList = assignPermissionIdList;
        }

        //4. 进行删除
        if (!CollectionUtils.isEmpty(removePermissionIdList)) {
            rolePermissionMapper.deletByRoleIdAndPermissionIdList(roleId,removePermissionIdList);
        }

        //二、重新分配
        //1. 判断permissionIds是否为空，如果不为空才进行重新分配
        if (!CollectionUtils.isEmpty(permissionIds)) {
            for (Long permissionId : permissionIds) {
                //2. 根据permissionId和roleId查询数据(不考虑is_deleted)
                RolePermission rolePermission = rolePermissionMapper.findByRoleIdAndPermissionId(roleId,permissionId);

                if (!ObjectUtils.isEmpty(rolePermission)) {
                    //2.1 如果查询出来的rolePermission不为空,说明曾经分配过
                    //2.1.1 继续判断is_deleted
                    if (rolePermission.getIsDeleted() == 1) {
                        //表示曾经分配过，但是删除了，我们就修改它的isDeleted=0
                        rolePermission.setIsDeleted(0);
                        rolePermissionMapper.update(rolePermission);
                    }
                }else {
                    //2.2 如果查询出来的rolePermission为空,说明曾经从未分配过
                    rolePermission = new RolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permissionId);
                    rolePermissionMapper.insert(rolePermission);
                }
            }
        }
    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        //1.1 判断当前用户是不是超级管理员,如果是超级管理员，则查询出所有权限
        List<Permission> permissionList = new ArrayList<>();
        if (adminId == 1) {
            permissionList = permissionMapper.findAll();
        }else {
            //1.2 如果当前用户不是超级管理员，才查询出当前用户的所有权限
            permissionList = permissionMapper.findPermissionListByAdminId(adminId);
        }

        //2. 构建父子菜单
        return PermissionHelper.build(permissionList);
    }

    @Override
    public List<String> findPermissionCodeListByAdminId(Long adminId) {
        List<Permission> permissionList = null;
        //1. 根据用户id查询用户的所有权限(如果是超级管理员，那么应该查询所有权限)
        if (adminId == 1) {
            permissionList = permissionMapper.findAll();
        }else {
            permissionList = permissionMapper.findPermissionListByAdminId(adminId);
        }
        //2. 过滤出三级菜单、并且将permission对象映射成String
        List<String> permissionCodeList = null;
        if (!CollectionUtils.isEmpty(permissionList)) {
            permissionCodeList = permissionList.stream()
                    .filter(permission -> permission.getType() == 2)
                    .map(Permission::getCode)
                    .collect(Collectors.toList());
        }
        return permissionCodeList;
    }
}
