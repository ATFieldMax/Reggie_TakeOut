package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.dto.SetmealDto;
import com.example.pojo.Setmeal;
import com.example.pojo.SetmealDish;
import com.example.service.CategoryService;
import com.example.service.SetmealDishService;
import com.example.service.SetmealService;
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
@RequestMapping("/setmeal")
@Transactional
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    //分頁查詢
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //建立分頁構造器
        Page<Setmeal> pageInfo=new Page(page,pageSize);
        Page<SetmealDto> setmealDtoPage=new Page(page,pageSize);

        //建立條件構造器
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();

        //添加過濾條件
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);

        //添加排序條件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //執行查詢
        setmealService.page(pageInfo,queryWrapper);

        //對象拷貝
        BeanUtils.copyProperties(pageInfo,setmealDtoPage);

        //調出套餐集合
        List<Setmeal> setmeals = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList=new ArrayList<>();

        for (Setmeal setmeal : setmeals) {
            SetmealDto setmealDto=new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            //使用分類id找出分類名稱
            //調用業務層查找分類名稱的方法
            Long categoryId = setmeal.getCategoryId();
            String categoryName = categoryService.findCategoryName(categoryId);
            setmealDto.setCategoryName(categoryName);
            setmealDtoList.add(setmealDto);
        }

        setmealDtoPage.setRecords(setmealDtoList);

        return R.success(setmealDtoPage);
    }

    //新增套餐
    @PostMapping
    public R<String> add(@RequestBody SetmealDto setmealDto){
        log.info("添加套餐，套餐信息:{}",setmealDto.toString());

        setmealService.addSetmeal(setmealDto);

        return R.success("添加成功!");
    }

    //根據id查詢套餐信息(頁面數據回顯)
    @GetMapping("/{id}")
    public R<SetmealDto> getbyId(@PathVariable Long id){
        log.info("根據id查詢套餐信息...");

        SetmealDto setmealDto = setmealService.getSetmealWithDish(id);

        return R.success(setmealDto);
    }

    //修改套餐
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("更新套餐，套餐信息:{}",setmealDto.toString());

        setmealService.updateSetmeal(setmealDto);

        return R.success("更新成功!");
    }

    //根據id修改套餐信息(啟售/停售)
    @PostMapping("/status/{$Setmeal.status}")
    public R<String> update1(Long ids){
        log.info("根據id修改套餐信息...");

        Setmeal setmeal = setmealService.getById(ids);

        if(setmeal.getStatus() == 0){
            setmeal.setStatus(1);
        }else{
            setmeal.setStatus(0);
        }

        setmealService.updateById(setmeal);

        return R.success("套餐信息修改成功!");
    }

    //根據id數組修改套餐信息(批量停售)
    @PostMapping("/status/0")
    public R<String> update2(Long[] ids){
        List<Setmeal> setmealList=new ArrayList<>();

        for(int i=0;i<ids.length;i++){
            Setmeal setmeal = setmealService.getById(ids[i]);

            setmeal.setStatus(0);

            setmealList.add(setmeal);
        }

        setmealService.updateBatchById(setmealList);

        return R.success("批量停售成功!");
    }

    //根據id數組修改套餐信息(批量啟售)
    @PostMapping("/status/1")
    public R<String> update3(Long[] ids){
        List<Setmeal> setmealList=new ArrayList<>();

        for(int i=0;i<ids.length;i++){
            Setmeal setmeal = setmealService.getById(ids[i]);

            setmeal.setStatus(1);

            setmealList.add(setmeal);
        }

        setmealService.updateBatchById(setmealList);

        return R.success("批量啟售成功!");
    }

    //刪除套餐(批量刪除)
    @DeleteMapping
    public R<String> delete(Long[] ids){
        for(int i=0;i<ids.length;i++){
            setmealService.removeById(ids[i]);

            //用ids找到SetmealDish對象
            //建立條件構造器
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            //添加查詢條件，根據ids進行查詢
            queryWrapper.eq(SetmealDish::getSetmealId,ids[i]);
            //執行查詢
            List<SetmealDish> list = setmealDishService.list(queryWrapper);
            //進行刪除操作
            for (SetmealDish setmealDish : list) {
                Long id = setmealDish.getId();
                log.info("SetmealDish要刪除的id為:{}",id);
                setmealDishService.removeById(id);
            }
        }
        return R.success("刪除成功!");
    }

    //移動端套餐展示
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        Integer status = setmeal.getStatus();
        Long categoryId = setmeal.getCategoryId();

        //使用分類id找到套餐對象
        List<Setmeal> setmealList = setmealService.findSetmealWithCategoryId(categoryId);

        return R.success(setmealList);
    }

    //根據id查詢套餐信息
    @GetMapping("/dish/{id}")
    public R<SetmealDto> getById(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getSetmealWithDish(id);

        log.info(setmealDto.toString());

        if(setmealDto != null){
            return R.success(setmealDto);
        }

        return R.error("沒有查詢到對應套餐信息");
    }
}
