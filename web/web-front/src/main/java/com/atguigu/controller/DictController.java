package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-10-07  09:28
 */
@RestController
@RequestMapping("/dict")
public class DictController {
    @Reference
    private DictService dictService;
    @GetMapping("/findDictListByParentDictCode/{parentDictCode}")
    public Result findDictListByParentDictCode(@PathVariable("parentDictCode") String parentDictCode){
        //调用业务层的方法根据父节点的dictCode查询子节点列表
        List<Dict> dictList = dictService.findDictListByParentDictCode(parentDictCode);

        return Result.ok(dictList);
    }

    @GetMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId") Long parentId){
        //调用业务层的方法根据父节点id查询子节点列表
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }
}
