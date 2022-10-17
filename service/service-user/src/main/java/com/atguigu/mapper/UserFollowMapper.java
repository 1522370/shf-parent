package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-10  09:22
 */
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    UserFollow findByUserIdAndHouseId(@Param("userId") Long userId, @Param("houseId") Long houseId);

    /**
     * 查询用户的关注列表
     * @param userId
     * @return
     */
    Page<UserFollowVo> findListPage(Long userId);
}
