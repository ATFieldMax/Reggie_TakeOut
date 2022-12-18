package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.SetmealDto;
import com.example.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //新增套餐
    public void addSetmeal(SetmealDto setmealDto);
    //查詢套餐
    public SetmealDto getSetmealWithDish(Long id);
    //更新套餐
    public void updateSetmeal(SetmealDto setmealDto);
    //使用分類id找到套餐對象
    public List<Setmeal> findSetmealWithCategoryId(Long categoryId);
}
