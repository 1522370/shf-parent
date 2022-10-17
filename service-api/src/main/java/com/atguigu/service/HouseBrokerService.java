package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseBroker;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-10-06  11:14
 */
public interface HouseBrokerService extends BaseService<HouseBroker> {
    List<HouseBroker> findHouseBrokerListByHouseId(Long houseId);
}
