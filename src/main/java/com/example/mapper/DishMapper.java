package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    //根據類別id查詢菜品
    @Select("select * from dish where category_id=#{categoryId}")
    public List<Dish> findDishWithCategoryId(Long categoryId);
}
