package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    //從菜品找出分類id，再使用使用分類id找出分類名稱
    @Select("select category.name from category where category.id=#{categoryId}")
    public String findCategoryName(Long categoryId);
}
