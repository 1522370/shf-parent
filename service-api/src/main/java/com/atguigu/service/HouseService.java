package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.House;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-10-05  15:45
 */
public interface HouseService extends BaseService<House> {

    PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo);

    /**
     * 查询最新上架的房源列表
     * @return
     */
    List<House> findLastedList();

}
