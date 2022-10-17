package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.constant.AtguiguConstant;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-10-10  09:22
 */
@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl extends BaseServiceImpl<UserFollow> implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Override
    public UserFollow findByUserIdAndHouseId(Long userId, Long houseId) {
        return userFollowMapper.findByUserIdAndHouseId(userId,houseId);
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userId) {
        //1. 开启分页
        PageHelper.startPage(pageNum,pageSize);
        //2. 调用持久层的方法查询当前用户的关注列表
        Page<UserFollowVo> userFollowVoList = userFollowMapper.findListPage(userId);
        return new PageInfo<>(userFollowVoList, AtguiguConstant.PageInfoConstant.DEFAULT_NAVIGATE_PAGES);
    }

    @Override
    public BaseMapper<UserFollow> getEntityMapper() {
        return userFollowMapper;
    }
}
