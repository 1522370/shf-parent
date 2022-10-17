package com.atguigu.base;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2022-09-30  15:02
 */
public interface BaseMapper<T> {
    /**
     * 查询所有
     * @return
     */
    List<T> findAll();

    /**
     * 保存
     * @param t
     */
    void insert(T t);

    /**
     * 根据主键进行删除
     * @param id
     */
    void delete(Long id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 修改
     * @param t
     */
    void update(T t);

    /**
     * 根据条件搜索
     * @param filters
     * @return
     */
    List<T> findPage(Map<String,Object> filters);
}
