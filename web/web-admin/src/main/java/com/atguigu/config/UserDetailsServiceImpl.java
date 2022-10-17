package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.config
 * @author Leevi
 * 日期2022-10-13  08:45
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username就是用户从登录页面传过来的username
        //1. 调用业务层的方法,根据用户名查找用户
        Admin admin = adminService.getByUsername(username);
        //1.1 如果admin为null，说明用户名错误
        if (ObjectUtils.isEmpty(admin)) {
            throw new UsernameNotFoundException("用户名错误");
        }
        //1.2 否则，就说明用户名正确，校验密码(将数据库中该用户的密码交给SpringSecurity)
        //1.3 授权: 查询到当前用户有哪些权限,然后将其告诉SpringSecurity
        //1.3.1 查询出当前用户有哪些权限(只需要查询三级菜单): List<String> ------> 怎么用一个字符串表示一个权限呢? ------> code字段的值
        List<String> permissionCodeList = permissionService.findPermissionCodeListByAdminId(admin.getId());
        //1.3.2 将当前用户拥有的权限告诉SpringSecurity: List<SimpleGrantedAuthority>

        List<SimpleGrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionCodeList)) {
            grantedAuthorityList = permissionCodeList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return new User(username, admin.getPassword(), grantedAuthorityList);
    }
}
