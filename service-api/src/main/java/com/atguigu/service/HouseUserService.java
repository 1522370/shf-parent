package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-10-06  11:19
 */
public interface HouseUserService extends BaseService<HouseUser> {
    List<HouseUser> findHouseUserListByHouseId(Long houseId);
}
