package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-06  11:20
 */
public interface HouseUserMapper extends BaseMapper<HouseUser> {
    List<HouseUser> findHouseUserListByHouseId(Long houseId);
}
