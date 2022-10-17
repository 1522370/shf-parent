package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import com.atguigu.service.RoleService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-10-11  11:16
 */
@Controller
public class IndexController {
    private static final String PAGE_INDEX = "frame/index";
    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;

    @GetMapping("/")
    public String index(Model model){
        //由SpringSecurity获取当前登录的用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //从user中获取账号username，然后根据username可以查询到admin
        Admin admin = adminService.getByUsername(user.getUsername());

        //1. 查询出当前登录的用户信息存储到请求域
        model.addAttribute("admin",admin);

        //2. 查询当前登录的用户的角色列表存储到请求域
        List<Role> roleList = roleService.findByAdminId(admin.getId());
        model.addAttribute("roleList",roleList);

        //3. 查询出当前登录的用户的动态菜单存储到请求域
        List<Permission> menu = permissionService.findMenuPermissionByAdminId(admin.getId());
        model.addAttribute("permissionList",menu);
        return PAGE_INDEX;
    }
}
