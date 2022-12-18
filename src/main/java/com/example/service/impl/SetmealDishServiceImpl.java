package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.SetmealDishMapper;
import com.example.pojo.SetmealDish;
import com.example.service.SetmealDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    //根據套餐id找到SetmealDish對象
    @Override
    public List<SetmealDish> findSetmealWithId(Long id) {
        return setmealDishMapper.findSetmealWithId(id);
    }

    //根據套餐id刪除SetmealDish對象
    @Override
    public void deleteBySetmealId(Long setmealId) {
        setmealDishMapper.deleteBySetmealId(setmealId);
    }

}
