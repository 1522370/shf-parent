package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.en.DictCodeEnum;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-10-05  10:22
 */
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {
    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_ACTION = "redirect:/community";
    @Reference
    private DictService dictService;
    @Reference
    private CommunityService communityService;
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //1. 调用业务层的方法查询小区的分页信息，并存储到请求域
        PageInfo<Community> page = communityService.findPage(filters);
        model.addAttribute("page",page);
        //2. 将搜索条件存储到请求域
        //判断如果filters中没有areaId，我们默认给它赋一个0
        if (ObjectUtils.isEmpty(filters.get("areaId"))) {
            filters.put("areaId",0);
        }
        //判断如果filters中没有plateId，我们默认给它赋一个0
        if (ObjectUtils.isEmpty(filters.get("plateId"))) {
            filters.put("plateId",0);
        }
        model.addAttribute("filters",filters);

        //3. 调用业务层的方法查询北京的所有区域，并存储到请求域
        List<Dict> areaList = dictService.findDictListByParentDictCode(DictCodeEnum.BEIJING.getCode());
        model.addAttribute("areaList",areaList);
        //4. 显示小区的首页
        return PAGE_INDEX;
    }

    @GetMapping("/create")
    public String create(Model model){
        //1. 查询北京的所有区域并存储到请求域
        List<Dict> dictList = dictService.findDictListByParentDictCode(DictCodeEnum.BEIJING.getCode());
        model.addAttribute("areaList",dictList);
        //2. 返回新增页面的逻辑视图
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Community community,Model model){
        //1. 调用业务层的方法保存数据
        communityService.insert(community);
        //2. 显示成功页面
        return successPage(model,"新增小区成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法根据id查询小区信息,并存储到请求域
        Community community = communityService.getById(id);
        model.addAttribute("community",community);
        //2. 查询北京的所有区域,并存储到请求域
        List<Dict> dictList = dictService.findDictListByParentDictCode(DictCodeEnum.BEIJING.getCode());
        model.addAttribute("areaList",dictList);
        //3.  返回修改页面的逻辑视图
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Community community,Model model){
        //1. 调用业务层的方法修改小区
        communityService.update(community);
        //2. 显示成功页面
        return successPage(model,"修改小区成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //1. 调用业务层的方法根据id删除小区
        communityService.delete(id);
        //2. 重定向访问小区的首页
        return LIST_ACTION;
    }
}
