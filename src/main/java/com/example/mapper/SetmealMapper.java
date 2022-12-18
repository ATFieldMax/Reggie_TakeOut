package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.Setmeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
    //使用分類id找到套餐對象
    @Select("select * from setmeal where category_id=#{categoryId} and status=1")
    public List<Setmeal> findSetmealWithCategoryId(Long categoryId);
}
