package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.CustomException;
import com.example.mapper.CategoryMapper;
import com.example.pojo.Category;
import com.example.pojo.Dish;
import com.example.pojo.Setmeal;
import com.example.service.CategoryService;
import com.example.service.DishService;
import com.example.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryMapper categoryMapper;

    //根據id刪除分類，刪除前先檢查是否關聯菜品或套餐
    @Override
    public void remove(Long id) {
        //添加查詢條件，根據分類id進行查詢
        LambdaQueryWrapper<Dish> dishqueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealqueryWrapper=new LambdaQueryWrapper<>();

        dishqueryWrapper.eq(Dish::getCategoryId, id);
        setmealqueryWrapper.eq(Setmeal::getCategoryId,id);

        int count = dishService.count(dishqueryWrapper);
        int count2=setmealService.count(setmealqueryWrapper);

        //查詢當前分類是否關聯菜品，如果有關聯，拋出一個業務異常
        if(count > 0){
            throw new CustomException("當前分類下關聯了菜品，不能刪除!");
        }
        //查詢當前分類是否關聯套餐，如果有關聯，拋出一個業務異常
        if(count2 > 0){
            throw new CustomException("當前分類下關聯了套餐，不能刪除!");
        }
        //正常刪除分類
        super.removeById(id);
    }

    //從菜品找出分類id，再使用使用分類id找出分類名稱
    @Override
    public String findCategoryName(Long categoryId) {
        return categoryMapper.findCategoryName(categoryId);
    }
}
