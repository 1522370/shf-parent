package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Role;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-09-29  11:46
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Override
    public BaseMapper<Role> getEntityMapper() {
        return roleMapper;
    }

    @Override
    public Map<String, List<Role>> findRoleListMapByAdminId(Long adminId) {
        //1. 查询所有角色
        List<Role> allRoleList = roleMapper.findAll();
        //2. 根据当前用户的id，查询当前用户已分配的所有角色的id
        List<Long> assignRoleIdList = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //3. 从allRoleList中筛选出用户已分配的角色列表和未分配的角色列表
        List<Role> assignRoleList = new ArrayList<>();
        List<Role> unAssignRoleList = new ArrayList<>();
        //3.1 遍历出每一个role
        for (Role role : allRoleList) {
            //3.2 判断如果role的id在assignRoleIdList,说明当前角色是已分配的
            if (assignRoleIdList.contains(role.getId())) {
                assignRoleList.add(role);
            }else {
                //说明当前角色是未分配的
                unAssignRoleList.add(role);
            }
        }

        //最后将assignRoleList和unAssignRoleList放到Map对象中返回
        Map<String,List<Role>> roleListMap = new HashMap<>();
        roleListMap.put("assignRoleList",assignRoleList);
        roleListMap.put("unAssignRoleList",unAssignRoleList);
        return roleListMap;
    }

    //简单做法
    /*@Override
    public void saveAdminRole(Long adminId, List<Long> roleIds) {
        //1. 删除当前用户已分配的所有角色
        adminRoleMapper.deleteByAdminId(adminId);
        //2. 将这次需要分配的角色进行新增
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                //新增
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(adminId);
                adminRole.setRoleId(roleId);

                adminRoleMapper.insert(adminRole);
            }
        }
    }*/

    //复杂做法
    @Override
    public void saveAdminRole(Long adminId, List<Long> roleIds) {
        //1. 找出需要删除的那些分配信息的角色id集合: 之前已分配、但是这次不要分配了
        //1.1 找出该用户之前已分配的角色的id集合
        List<Long> assignRoleIdList = adminRoleMapper.findRoleIdListByAdminId(adminId);
        //声明一个集合用来存储要删除的那些分配信息的角色id集合
        List<Long> removeRoleIdList = new ArrayList<>();
        //1.2 判断assignRoleIdList是否为空
        if (!CollectionUtils.isEmpty(assignRoleIdList)) {
            //1.3 如果assignRoleIdList不为空,则获取需要删除的分配信息的角色id集合
            //1.3.1 判断roleIds是否为空
            if (CollectionUtils.isEmpty(roleIds)) {
                //roleIds为空,表示将之前已分配的全部删掉
                removeRoleIdList = assignRoleIdList;
            }else {
                //roleIds不为空，那么如果在assignRoleIdList但是不在roleIds就表示要删除
                for (Long roleId : assignRoleIdList) {
                    if (!roleIds.contains(roleId)) {
                        removeRoleIdList.add(roleId);
                    }
                }
            }
        }
        //1.3 调用调用业务层的方法将该删除的进行删除
        if (!CollectionUtils.isEmpty(removeRoleIdList)) {
            adminRoleMapper.deleteByAdminIdAndRoleIdList(adminId,removeRoleIdList);
        }

        //2. 找出这次需要新增的
        //2.1 根据当前用户的id，查询出当前用户已分配的所有信息(包含被删除了的)
        if (!CollectionUtils.isEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                //在roleIds中，但是不在assignRoleIdList中，就表示这就是这次需要新增的
                AdminRole adminRole = adminRoleMapper.findByAdminIdAndRoleId(adminId,roleId);
                if (adminRole != null) {
                    //说明曾经分配过
                    //要查询是否已经被删除了
                    if (adminRole.getIsDeleted() == 1) {
                        //说明被删除了，只需要将isDeleted修改为0
                        adminRole.setIsDeleted(0);
                        adminRoleMapper.update(adminRole);
                    }
                }else {
                    //说明从未分配过,则执行新增
                    adminRole = new AdminRole();
                    adminRole.setRoleId(roleId);
                    adminRole.setAdminId(adminId);

                    adminRoleMapper.insert(adminRole);
                }
            }
        }
    }

    @Override
    public List<Role> findByAdminId(Long adminId) {
        return roleMapper.findByAdminId(adminId);
    }
}
