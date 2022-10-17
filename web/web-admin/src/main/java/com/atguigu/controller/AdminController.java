package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-09-30  15:49
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    private static final String PAGE_INDEX = "admin/index";
    private static final String LIST_ACTION = "redirect:/admin";
    private static final String PAGE_EDIT = "admin/edit";
    private static final String PAGE_UPLOAD_SHOW = "admin/upload";
    private static final String PAGE_ASSIGN_SHOW = "admin/assignShow";
    @Reference
    private AdminService adminService;
    @Reference
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //1. 调用业务层的方法查询分页数据
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        //2. 将分页数据存储到请求域
        model.addAttribute("page",pageInfo);
        //3. 将搜索条件存储到请求域
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }

    @PostMapping("/save")
    public String save(Admin admin,Model model){
        //0. 使用SpringSecurity所使用的加密器对admin中的密码进行加密
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        //1. 调用业务层的方法保存用户信息
        adminService.insert(admin);
        //2. 显示成功页面
        return successPage(model,"保存用户信息成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //1. 调用业务层的方法根据id删除
        adminService.delete(id);
        //2. 重定向访问首页
        return LIST_ACTION;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法根据id查询
        Admin admin = adminService.getById(id);
        //2. 将查询到的用户信息存储到请求域
        model.addAttribute("admin",admin);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Admin admin,Model model){
        //1. 调用业务层的方法修改用户
        adminService.update(admin);
        //2. 显示成功页面
        return successPage(model,"修改用户信息成功");
    }

    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id,Model model){
        //将id存储到请求域
        model.addAttribute("id",id);
        return PAGE_UPLOAD_SHOW;
    }

    @PostMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id,
                         @RequestParam("file") MultipartFile multipartFile,
                         Model model) throws IOException {
        //将图片上传到七牛云
        //1. 获取一个唯一的文件名
        String uuidName = FileUtil.getUUIDName(multipartFile.getOriginalFilename());
        //2. 获取文件存储路径
        String dateDirPath = FileUtil.getDateDirPath();
        String fileName = dateDirPath + uuidName;
        QiniuUtils.upload2Qiniu(multipartFile.getBytes(),fileName);
        //3. 获取文件的访问路径
        String url = QiniuUtils.getUrl(fileName);
        //4. 根据id获取当前用户信息
        Admin admin = adminService.getById(id);
        admin.setHeadUrl(url);
        //修改用户信息
        adminService.update(admin);

        //2. 显示成功页面
        return successPage(model,"头像上传成功");
    }

    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法查询用户已分配和未分配的角色列表
        Map<String, List<Role>> roleListMap = roleService.findRoleListMapByAdminId(id);
        //2. 将查询到的数据存储到请求域,model调用addAllAttributes(map),就会将map中的所有键值对作为请求域的键值对存起来
        model.addAllAttributes(roleListMap);
        //3. 将用户id存储到请求域
        model.addAttribute("adminId",id);
        //4. 显示视图
        return PAGE_ASSIGN_SHOW;
    }

    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds,
                             Model model){
        //adminId就是当前要分配角色的用户的id
        //roleIds就是要分配给用户的那些角色的id集合
        //调用业务层的方法给用户分配角色
        roleService.saveAdminRole(adminId,roleIds);
        //显示成功页面
        return successPage(model,"保存角色成功");
    }
}
