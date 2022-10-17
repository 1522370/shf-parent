package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-10-05  08:54
 */
@RestController
@RequestMapping("/dict")
public class DictController {
    @Reference
    private DictService dictService;


    @GetMapping("/findZnodes")
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0") Long id){
        //1. 调用业务层的方法以id的值作为parentId查询子节点列表
        List<Map<String, Object>> zNodes = dictService.findZnodes(id);
        return Result.ok(zNodes);
    }


    @GetMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId") Long parentId){
        //1. 调用业务层的方法根据parentId查询子节点列表
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }
}
