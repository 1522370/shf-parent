package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.AtguiguConstant;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-10-10  10:07
 */
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {
    @Reference
    private UserFollowService userFollowService;

    @GetMapping("/auth/follow/{houseId}")
    public Result addFollow(@PathVariable("houseId") Long houseId, HttpSession session){
        //调用业务层的方法添加关注: 其实就是往user_follow表中插入一条数据
        //1. 创建UserFollow对象
        UserFollow userFollow = new UserFollow();
        //2. 设置属性
        //2.1 属性houseId
        userFollow.setHouseId(houseId);
        //2.2 设置userId
        UserInfo userInfo = (UserInfo) session.getAttribute(AtguiguConstant.SessionConstant.USER_KEY);
        userFollow.setUserId(userInfo.getId());
        //3. 调用业务层的方法添加关注
        userFollowService.insert(userFollow);

        return Result.ok();
    }

    @GetMapping("/auth/list/{pageNum}/{pageSize}")
    public Result followList(@PathVariable("pageNum") Integer pageNum,
                             @PathVariable("pageSize") Integer pageSize,
                             HttpSession session){
        //获取当前登录的用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute(AtguiguConstant.SessionConstant.USER_KEY);
        //调用业务层的方法获取分页的关注数据
        PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(pageNum,pageSize,userInfo.getId());
        //返回结果
        return Result.ok(pageInfo);
    }

    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id){
        //调用业务层的方法删除数据
        userFollowService.delete(id);

        return Result.ok();
    }
}
