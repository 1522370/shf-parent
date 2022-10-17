package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-06  11:16
 */
public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {
    List<HouseBroker> findHouseBrokerListByHouseId(Long houseId);
}
