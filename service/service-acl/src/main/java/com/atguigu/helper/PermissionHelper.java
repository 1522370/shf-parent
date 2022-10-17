package com.atguigu.helper;

import com.atguigu.entity.Permission;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.helper
 *
 * @author Leevi
 * 日期2022-10-11  14:18
 */
public class PermissionHelper {
    /**
     * 设置菜单的父子结构
     * @param permissionList
     * @return
     */
    public static List<Permission> build(List<Permission> permissionList) {
        List<Permission> menu = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionList)) {
            //1. 从当前用户的所有权限中筛选出一级菜单
            menu = permissionList.stream()
                    .filter(permission -> permission.getParentId() == 0)
                    .collect(Collectors.toList());

            //2. 对每一个一级菜单构建子菜单
            //3.1 遍历出menu中的每一个一级菜单
            for (Permission permission : menu) {
                //3.2 从permissionList中找出当前这个permission的子菜单
                //设置permission的level为1
                permission.setLevel(1);
                setChildren(permissionList, permission);
            }
        }
        return menu;
    }

    /**
     * 从permissionList中找出permission的所有子菜单，设置给permission
     * @param permissionList
     * @param permission
     */
    public static void setChildren(List<Permission> permissionList, Permission permission) {
        //创建一个集合用来存储permission的子菜单
        List<Permission> children = new ArrayList<>();
        for (Permission child : permissionList) {
            if (child.getParentId().equals(permission.getId())) {
                //设置子菜单的层级为: 父菜单的层级 + 1
                child.setLevel(permission.getLevel() + 1);
                child.setParentName(permission.getName());
                //说明child是permission的子菜单
                children.add(child);

                //给child又设置子菜单
                setChildren(permissionList,child);
            }
        }
        //设置permission的子菜单
        permission.setChildren(children);
    }
}
