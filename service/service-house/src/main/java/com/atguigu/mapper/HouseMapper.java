package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-05  15:16
 */
public interface HouseMapper extends BaseMapper<House> {
    /**
     * 根据小区id查询房源的数量
     * @param communityId
     * @return
     */
    Long findCountByCommunityId(Long communityId);

    /**
     * 搜索前台房源列表数据
     * @param houseQueryVo
     * @return
     */
    Page<HouseVo> findListPage(HouseQueryVo houseQueryVo);

    /**
     * 查询最新上架的房源列表
     * @return
     */
    List<House> findLastedList();
}
