package com.example.service;
import com.example.service.CategoryService;
import com.example.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestFindCategoryId {
    @Autowired
    private CategoryService categoryService;

    @Test
    void test(){
        String categoryName = categoryService.findCategoryName(Long.valueOf("1413341197421846529"));
        System.out.println(categoryName);
    }
}
