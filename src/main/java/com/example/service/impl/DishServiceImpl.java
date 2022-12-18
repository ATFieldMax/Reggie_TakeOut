package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.DishDto;
import com.example.mapper.DishMapper;
import com.example.pojo.Dish;
import com.example.pojo.DishFlavor;
import com.example.service.DishFlavorService;
import com.example.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishMapper dishMapper;

    //新增菜品，同時插入口味數據
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息至菜品表
        this.save(dishDto);

        //取得菜品id
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            //設置菜品id
            flavor.setDishId(dishId);
        }

        //保存口味數據至菜品口味表
        dishFlavorService.saveBatch(flavors);
    }

    //查詢帶有口味數據的菜品
    @Override
    public DishDto getDishWithFlavor(Long id) {
        Dish dish=this.getById(id);

        DishDto dishDto=new DishDto();

        BeanUtils.copyProperties(dish,dishDto);

        List<DishFlavor> list = dishFlavorService.findDishFlavor(id);
        dishDto.setFlavors(list);

        return dishDto;
    }

    //修改帶有口味數據的菜品
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //在菜品表中更新基本信息
        this.updateById(dishDto);

        List<DishFlavor> flavors = dishDto.getFlavors();

        //更新口味數據至菜品口味表
        dishFlavorService.updateBatchById(flavors);
    }

    //根據類別id查詢菜品
    @Override
    public List<Dish> findDishWithCategoryId(Long categoryId) {
        return dishMapper.findDishWithCategoryId(categoryId);
    }
}
