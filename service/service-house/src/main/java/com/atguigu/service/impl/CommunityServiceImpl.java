package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Community;
import com.atguigu.mapper.CommunityMapper;
import com.atguigu.mapper.HouseMapper;
import com.atguigu.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-10-05  10:34
 */
@Service(interfaceClass = CommunityService.class)
public class CommunityServiceImpl extends BaseServiceImpl<Community> implements CommunityService {
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private HouseMapper houseMapper;
    @Override
    public BaseMapper<Community> getEntityMapper() {
        return communityMapper;
    }

    @Override
    public void delete(Long id) {
        //1. 判断当前id对应的小区内是否有房源
        Long houseCount = houseMapper.findCountByCommunityId(id);
        if (houseCount > 0) {
            //说明小区内有房源，不能删除
            throw new RuntimeException("小区内有房源，不能删除!!!");
        }
        //否则说明小区内没有房源，则可以删除
        super.delete(id);
    }
}
