package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-10-05  09:10
 */
public interface DictService extends BaseService<Dict> {
    /**
     * 根据父节点的id查询子节点的树形结构数据
     * @param parentId
     * @return
     */
    List<Map<String,Object>> findZnodes(Long parentId);

    /**
     * 根据父节点的dictCode查询子节点列表
     * @param parentDictCode
     * @return
     */
    List<Dict> findDictListByParentDictCode(String parentDictCode);

    /**
     * 根据父节点id查询子节点列表
     * @param parentId
     * @return
     */
    List<Dict> findDictListByParentId(Long parentId);
}
