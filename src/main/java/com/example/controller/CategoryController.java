package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.pojo.Category;
import com.example.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
@Transactional
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //新增分類
    @PostMapping
    public R<String> add(@RequestBody Category category){
        log.info("category:{}",category.toString());

        categoryService.save(category);

        return R.success("添加成功!");
    }

    //分頁查詢
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //建立分頁構造器
        Page pageInfo=new Page(page,pageSize);

        //建立條件構造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();

        //添加過濾條件
        queryWrapper.like(StringUtils.isNotEmpty(name),Category::getName,name);

        //添加排序條件
        queryWrapper.orderByAsc(Category::getSort);

        //執行查詢
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    //刪除分類
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("根據id刪除分類...");

        //categoryService.removeById(ids);
        categoryService.remove(ids);

        return R.success("刪除成功!");
    }

    //修改分類
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分類信息:{}",category);

        categoryService.updateById(category);

        return R.success("修改成功!");
    }

    //查詢菜品分類(下拉式選單)、移動端展示菜品數據
    @GetMapping("/list")
    public R<List<Category>> list(Category category){ //使用對象接收參數，將type屬性封裝進Category
        //建立條件構造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加查詢條件，根據type進行查詢
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序條件
        queryWrapper.orderByAsc(Category::getSort);
        //執行查詢
        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }


}
