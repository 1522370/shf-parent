package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.en.DictCodeEnum;
import com.atguigu.en.HouseImageType;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.*;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-10-05  15:41
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;

    @PreAuthorize("hasAuthority('house.show')")
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //1. 查询房源的分页信息,并存储到请求域
        PageInfo<House> page = houseService.findPage(filters);
        model.addAttribute("page",page);
        //2. 将搜索条件存储到请求域
        model.addAttribute("filters",filters);
        //3. 初始化数据
        initData(model);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('house.create')")
    @GetMapping("/create")
    public String create(Model model){
        //1. 初始化数据
        initData(model);
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('house.create')")
    @PostMapping("/save")
    public String save(House house,Model model){
        //1.房源表中有一个status字段,这个字段的值在新增页面中没有传过来,而且这个字段的值又不能为null,所以我们在新增房源前需要给status赋值
        //status字段表示房源是否已发布:0表示未发布、1表示已发布
        house.setStatus(HouseStatus.UNPUBLISHED.getCode());
        //2. 调用业务层的方法保存房源信息
        houseService.insert(house);
        //3. 显示成功页面
        return successPage(model,"新增房源成功");
    }

    @PreAuthorize("hasAuthority('house.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法根据id查询房源信息,并存储到请求域
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        //2. 初始化数据
        initData(model);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('house.edit')")
    @PostMapping("/update")
    public String update(House house,Model model){
        //1. 调用业务层的方法修改房源信息
        houseService.update(house);
        //2. 显示成功页面
        return successPage(model,"修改房源成功");
    }

    @PreAuthorize("hasAuthority('house.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //1. 调用业务层的方法删除房源
        houseService.delete(id);
        //2. 重定向显示房源的首页
        return LIST_ACTION;
    }

    @PreAuthorize("hasAuthority('house.publish')")
    @GetMapping("/publish/{id}/{status}")
    public String publishOrUnPublish(@PathVariable("id") Long id,
                                     @PathVariable("status") Integer status){
        //1. 创建House对象,并且设置house的id和status
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        //2. 调用业务层的方法修改房源信息
        houseService.update(house);
        //3. 重定向访问房源的首页
        return LIST_ACTION;
    }

    @PreAuthorize("hasAuthority('house.show')")
    @GetMapping("/{houseId}")
    public String detail(@PathVariable("houseId") Long houseId,Model model){
        //1. 查询房源信息存储到请求域
        House house = houseService.getById(houseId);
        model.addAttribute("house",house);
        //2. 查询小区信息存储到请求域
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("community",community);
        //3. 查询房源图片列表存储到请求域
        List<HouseImage> houseImage1List = houseImageService.findHouseImageListByHouseId(houseId, HouseImageType.HOUSE_SOURCE.getCode());
        model.addAttribute("houseImage1List",houseImage1List);
        //4. 查询房产图片列表存储到请求域
        List<HouseImage> houseImage2List = houseImageService.findHouseImageListByHouseId(houseId, HouseImageType.HOUSE_PROPERTY.getCode());
        model.addAttribute("houseImage2List",houseImage2List);
        //5. 查询房源经纪人列表存储到请求域
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(houseId);
        model.addAttribute("houseBrokerList",houseBrokerList);
        //6. 查询房东列表存储到请求域
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(houseId);
        model.addAttribute("houseUserList",houseUserList);

        return PAGE_SHOW;
    }
    /**
     * 初始化数据
     * @param model
     */
    private void initData(Model model) {
        //查询所有的小区,并存储到请求域
        List<Community> communityList = communityService.findAll();
        model.addAttribute("communityList", communityList);
        //查询所有的户型,并存储到请求域
        List<Dict> houseTypeList = dictService.findDictListByParentDictCode(DictCodeEnum.HOUSETYPE.getCode());
        model.addAttribute("houseTypeList", houseTypeList);
        //查询所有的楼层,并存储到请求域
        List<Dict> floorList = dictService.findDictListByParentDictCode(DictCodeEnum.FLOOR.getCode());
        model.addAttribute("floorList", floorList);
        //查询所有的朝向,并存储到请求域
        List<Dict> directionList = dictService.findDictListByParentDictCode(DictCodeEnum.DIRECTION.getCode());
        model.addAttribute("directionList", directionList);
        //查询所有的装修情况,并存储到请求域
        List<Dict> decorationList = dictService.findDictListByParentDictCode(DictCodeEnum.DECORATION.getCode());
        model.addAttribute("decorationList", decorationList);
        //查询所有的建筑结构,并存储到请求域
        List<Dict> buildStructureList = dictService.findDictListByParentDictCode(DictCodeEnum.BUILDSTRUCTURE.getCode());
        model.addAttribute("buildStructureList", buildStructureList);
        //查询所有的房屋用途,并存储到请求域
        List<Dict> houseUseList = dictService.findDictListByParentDictCode(DictCodeEnum.HOUSEUSE.getCode());
        model.addAttribute("houseUseList", houseUseList);
    }
}
