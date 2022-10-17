package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseImage;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-10-06  11:06
 */
public interface HouseImageService extends BaseService<HouseImage> {
    List<HouseImage> findHouseImageListByHouseId(Long houseId,Integer type);
}
