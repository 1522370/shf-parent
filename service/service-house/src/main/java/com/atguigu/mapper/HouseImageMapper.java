package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-10-06  11:08
 */
public interface HouseImageMapper extends BaseMapper<HouseImage> {
    List<HouseImage> findHouseImageListByHouseId(@Param("houseId") Long houseId,@Param("type") Integer type);
}
