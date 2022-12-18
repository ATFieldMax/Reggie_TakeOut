package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);

    //從菜品找出分類id，再使用使用分類id找出分類名稱
    public String findCategoryName(Long categoryId);
}
