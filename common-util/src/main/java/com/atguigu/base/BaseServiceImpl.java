package com.atguigu.base;

import com.atguigu.constant.AtguiguConstant;
import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2022-09-30  15:12
 */
public abstract class BaseServiceImpl<T> {
    public abstract BaseMapper<T> getEntityMapper();

    public List<T> findAll() {
        return getEntityMapper().findAll();
    }


    public void insert(T t) {
        getEntityMapper().insert(t);
    }


    public void delete(Long id) {
        getEntityMapper().delete(id);
    }


    public T getById(Long id) {
        return getEntityMapper().getById(id);
    }


    public void update(T t) {
        getEntityMapper().update(t);
    }


    public PageInfo<T> findPage(Map<String, Object> filters) {
        //1. 获取当前页数和每页数据条数并且转成int类型
        //1.1 pageNum默认值是1
        int pageNum = CastUtil.castInt(filters.get("pageNum"), AtguiguConstant.PageInfoConstant.DEFAULT_PAGE_NUM);
        //1.2 pageSize默认值是5
        int pageSize = CastUtil.castInt(filters.get("pageSize"), AtguiguConstant.PageInfoConstant.DEFAULT_PAGE_SIZE);
        //2. 开启分页
        PageHelper.startPage(pageNum,pageSize);
        //3. 调用持久层的方法根据条件进行搜索
        List<T> list = getEntityMapper().findPage(filters);

        return new PageInfo<T>(list,AtguiguConstant.PageInfoConstant.DEFAULT_NAVIGATE_PAGES);
    }
}
