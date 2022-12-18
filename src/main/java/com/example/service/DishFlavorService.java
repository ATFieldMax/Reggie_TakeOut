package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.DishFlavor;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    //使用菜品id找到DishFlavor對象
    public List<DishFlavor> findDishFlavor(Long id);
}
