package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    //根據套餐id找到SetmealDish對象
    public List<SetmealDish> findSetmealWithId(Long id);

    //根據套餐id刪除SetmealDish對象
    public void deleteBySetmealId(Long setmealId);
}
