package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.constant.AtguiguConstant;
import com.atguigu.entity.House;
import com.atguigu.entity.vo.HouseQueryVo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.HouseService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-10-05  15:45
 */
@Service(interfaceClass = HouseService.class)
public class HouseServiceImpl extends BaseServiceImpl<House> implements HouseService {
    @Autowired
    private HouseMapper houseMapper;
    @Override
    public BaseMapper<House> getEntityMapper() {
        return houseMapper;
    }

    @Override
    public PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryVo houseQueryVo) {
        //1.开启分页
        PageHelper.startPage(pageNum,pageSize);
        //2.调用持久层的方法搜索分页的列表数据
        Page<HouseVo> houseVoList =  houseMapper.findListPage(houseQueryVo);
        return new PageInfo<>(houseVoList, AtguiguConstant.PageInfoConstant.DEFAULT_NAVIGATE_PAGES);
    }

    @Override
    public List<House> findLastedList() {
        return houseMapper.findLastedList();
    }
}
