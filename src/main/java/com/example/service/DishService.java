package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.DishDto;
import com.example.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    //新增菜品，同時插入口味數據
    public void saveWithFlavor(DishDto dishDto);

    //查詢帶有口味數據的菜品
    public DishDto getDishWithFlavor(Long id);

    //修改帶有口味數據的菜品
    public void updateWithFlavor(DishDto dishDto);

    //根據類別id查詢菜品
    public List<Dish> findDishWithCategoryId(Long categoryId);
}
