package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
    //使用菜品id找到DishFlavor對象
    @Select("select  * from dish_flavor where dish_id=#{id}")
    public List<DishFlavor> findDishFlavor(Long id);
}
