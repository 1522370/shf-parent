package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Dict;
import com.atguigu.mapper.DictMapper;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-10-05  09:14
 */
@Service(interfaceClass = DictService.class)
public class DictServiceImpl extends BaseServiceImpl<Dict> implements DictService {
    @Autowired
    private DictMapper dictMapper;
    @Override
    public BaseMapper<Dict> getEntityMapper() {
        return dictMapper;
    }

    @Override
    public List<Map<String,Object>> findZnodes(Long parentId) {
        //1. 调用持久层的方法根据父节点id查询子节点列表
        List<Dict> dictList = dictMapper.findDictListByParentId(parentId);
        //2. 封装数据
        //code就是200、message就是成功、ok就是true、data就是你第二步封装好的数据

        //使用原始for循环
        /*List<Map<String,Object>> zNodes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dictList)) {
            for (Dict dict : dictList) {
                //每一个dict对应创建一个map对象
                Map<String,Object> map = new HashMap<>();
                //往map中存储name
                map.put("name",dict.getName());
                //往map中存储id
                map.put("id",dict.getId());
                //往map中存储isParent,如果当前节点还有子节点就设置isParent为true，否则就为false
                map.put("isParent",!CollectionUtils.isEmpty(dictMapper.findDictListByParentId(dict.getId())));
                zNodes.add(map);
            }
        }*/

        //使用Stream流进行操作
        List<Map<String, Object>> zNodes = null;
        if (!CollectionUtils.isEmpty(dictList)) {
            zNodes = dictList.stream()
                    .map(dict -> {
                        //每一个dict对应创建一个map对象
                        Map<String, Object> map = new HashMap<>();
                        //往map中存储name
                        map.put("name", dict.getName());
                        //往map中存储id
                        map.put("id", dict.getId());
                        //往map中存储isParent,如果当前节点还有子节点就设置isParent为true，否则就为false
                        map.put("isParent", !CollectionUtils.isEmpty(dictMapper.findDictListByParentId(dict.getId())));
                        return map;
                    })
                    .collect(Collectors.toList());
        }
        return zNodes;
    }

    @Override
    public List<Dict> findDictListByParentDictCode(String parentDictCode) {
        return dictMapper.findDictListByParentDictCode(parentDictCode);
    }

    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        return dictMapper.findDictListByParentId(parentId);
    }
}
