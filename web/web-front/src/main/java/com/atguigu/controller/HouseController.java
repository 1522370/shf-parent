package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.en.HouseImageType;
import com.atguigu.entity.*;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 * @author Leevi
 * 日期2022-10-07  09:59
 * JavaBean的分类:
 *  1. POJO: 通常用于与数据库进行交互(携带数据给数据库、接收数据库查询出来的数据)
 *  2. VO: 通常用于与视图进行交互(接收客户端携带的请求参数、封装给客户端的响应数据)
 *  3. TO: 通常用于分布式架构中远程调用的时候传输数据
 *  4. BO: 它里边除了可以封装数据,还会有一些业务操作方法
 */
@RestController
@RequestMapping("/house")
public class HouseController {
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private UserFollowService userFollowService;
    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result list(@PathVariable("pageNum") Integer pageNum,
                       @PathVariable("pageSize") Integer pageSize,
                       @RequestBody HouseQueryVo houseQueryVo){
        //调用业务层的方法查询前台房源的分页数据
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum, pageSize, houseQueryVo);
        return Result.ok(pageInfo);
    }

    @GetMapping("/lastedList")
    public Result lastedList(){
        //调用业务层的方法查询最新上架的房源列表
        List<House> lastedList = houseService.findLastedList();

        return Result.ok(lastedList);
    }

    @GetMapping("/info/{houseId}")
    public Result info(@PathVariable("houseId") Long houseId, HttpSession session){
        //调用业务层的方法查询房源详情
        //1. 查询房源信息
        House house = houseService.getById(houseId);
        //2. 查询小区信息
        Community community = communityService.getById(house.getCommunityId());
        //3. 经纪人列表
        List<HouseBroker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(houseId);
        //4. 房源图片列表
        List<HouseImage> houseImage1List = houseImageService.findHouseImageListByHouseId(houseId, HouseImageType.HOUSE_SOURCE.getCode());
        //5. 当前用户是否关注了这个房源
        //5.1 判断当前用户是否已登录
        boolean isFollow = false;
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        if (userInfo != null) {
            //已登录:根据userId以及houseId到user_follow表查询数据
            if (userFollowService.findByUserIdAndHouseId(userInfo.getId(),houseId) != null) {
                isFollow = true;
            }
        }
        //将上述五个数据封装到Map对象
        Map resultMap = new HashMap();
        resultMap.put("isFollow",isFollow);

        resultMap.put("houseBrokerList",houseBrokerList);
        resultMap.put("houseImage1List",houseImage1List);
        resultMap.put("community",community);
        resultMap.put("house",house);
        return Result.ok(resultMap);
    }
}
