package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.dto.DishDto;
import com.example.pojo.Dish;
import com.example.pojo.DishFlavor;
import com.example.service.CategoryService;
import com.example.service.DishFlavorService;
import com.example.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
@Transactional
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    //分頁查詢
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //建立分頁構造器
        Page<Dish> pageInfo=new Page(page,pageSize);
        Page<DishDto> dishDtoPage=new Page(page,pageSize);

        //建立條件構造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();

        //添加過濾條件
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);

        //添加排序條件
        queryWrapper.orderByAsc(Dish::getCategoryId);

        //執行查詢
        dishService.page(pageInfo,queryWrapper);

        //對象拷貝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        //調出菜品集合
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list=new ArrayList<>();

        for (Dish record : records) {
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(record,dishDto);
            //從菜品找出分類id
            Long categoryId = record.getCategoryId();
            //使用分類id找出分類名稱
            //調用業務層查找分類名稱的方法
            String categoryName = categoryService.findCategoryName(categoryId);
            dishDto.setCategoryName(categoryName);
            list.add(dishDto);
        }

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    //添加菜品
    @PostMapping
    public R<String> add(@RequestBody DishDto dishDto){
        log.info("添加菜品，菜品信息:{}",dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("添加成功!");
    }

    //根據id查詢菜品信息(頁面數據回顯)
    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        log.info("根據id查詢菜品信息...");

        //調用業務層方法
        DishDto dishDto = dishService.getDishWithFlavor(id);

        if(dishDto != null){
            return R.success(dishDto);
        }
        return R.error("沒有查詢到對應菜品信息");
    }

    //修改菜品
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("更新菜品，菜品信息:{}",dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("更新成功!");
    }

    //根據id修改菜品信息(啟售/停售)
    @PostMapping("/status/{$Dish.status}")
    public R<String> update1(Long ids){
        log.info("根據id修改菜品信息...");

        Dish dish = dishService.getById(ids);

        if(dish.getStatus() == 0){
            dish.setStatus(1);
        }else{
            dish.setStatus(0);
        }

        log.info(dish.toString());

        dishService.updateById(dish);

        return R.success("菜品信息修改成功!");
    }

    //根據id數組修改菜品信息(批量停售)
    @PostMapping("/status/0")
    public R<String> update2(Long[] ids){
        List<Dish> dishList=new ArrayList<>();

        for(int i=0;i<ids.length;i++){

            Dish dish = dishService.getById(ids[i]);

            dish.setStatus(0);

            dishList.add(dish);
        }

        dishService.updateBatchById(dishList);

        return R.success("批量停售成功!");
    }

    //根據id數組修改菜品信息(批量啟售)
    @PostMapping("/status/1")
    public R<String> update3(Long[] ids){
        List<Dish> dishList=new ArrayList<>();

        for(int i=0;i<ids.length;i++){

            Dish dish = dishService.getById(ids[i]);

            dish.setStatus(1);

            dishList.add(dish);
        }

        dishService.updateBatchById(dishList);

        return R.success("批量啟售成功!");
    }

    //刪除菜品(批量刪除)
    @DeleteMapping
    public R<String> delete2(Long[] ids){
        for(int i=0;i<ids.length;i++){
            dishService.removeById(ids[i]);
            //用ids(dish_id)找到口味
            //建立條件構造器
            LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
            //添加查詢條件，根據ids進行查詢
            queryWrapper.eq(DishFlavor::getDishId, ids[i]);
            //執行查詢
            List<DishFlavor> list = dishFlavorService.list(queryWrapper);
            //進行刪除操作
            for (DishFlavor dishFlavor : list) {
                Long id = dishFlavor.getId();
                dishFlavorService.removeById(id);
            }
        }

        return R.success("刪除成功!");
    }

    //添加菜品(套餐管理/新建套餐)、移動端展示菜品數據
    @GetMapping("/list")
    public R<List<DishDto>> add(Long categoryId){
        List<DishDto> dishDtoList=new ArrayList<>();

        //使用分賴id找到dish對象
        List<Dish> dishList = dishService.findDishWithCategoryId(categoryId);

        //加入口味數據
        for (Dish dish : dishList) {
            DishDto dishDto=new DishDto();

            //對象拷貝
            BeanUtils.copyProperties(dish,dishDto);

            //使用菜品id找到口味對象
            Long dishId = dish.getId();
            List<DishFlavor> dishFlavor = dishFlavorService.findDishFlavor(dishId);

            dishDto.setFlavors(dishFlavor);
            dishDtoList.add(dishDto);
        }

        return R.success(dishDtoList);
    }
}
