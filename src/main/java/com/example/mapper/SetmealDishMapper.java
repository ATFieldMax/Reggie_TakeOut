package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
    //根據套餐id找到SetmealDish對象
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    public List<SetmealDish> findSetmealWithId(Long id);

    //根據套餐id刪除SetmealDish對象
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    public void deleteBySetmealId(Long setmealId);
}
