package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.DishFlavorMapper;
import com.example.pojo.DishFlavor;
import com.example.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    //使用菜品id找到DishFlavor對象
    @Override
    public List<DishFlavor> findDishFlavor(Long id) {
        return dishFlavorMapper.findDishFlavor(id);
    }

}
