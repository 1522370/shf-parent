package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-10-10  09:20
 */
public interface UserFollowService extends BaseService<UserFollow> {
    /**
     * 根据用户id和房源id查询用户关注房源的记录
     * @param userId
     * @param houseId
     * @return
     */
    UserFollow findByUserIdAndHouseId(Long userId,Long houseId);

    /**
     * 查询用户的分页关注信息
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    PageInfo<UserFollowVo> findListPage(Integer pageNum, Integer pageSize, Long userId);
}
