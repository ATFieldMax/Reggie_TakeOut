package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.SetmealDto;
import com.example.mapper.SetmealMapper;
import com.example.pojo.Setmeal;
import com.example.pojo.SetmealDish;
import com.example.service.SetmealDishService;
import com.example.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealMapper setmealMapper;

    //新增套餐
    @Override
    public void addSetmeal(SetmealDto setmealDto) {
        //保存套餐的基本信息至套餐表
        this.save(setmealDto);

        //取得套餐id
        Long setmealId = setmealDto.getId();

        //取得套餐內的菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            //設置套餐id
            setmealDish.setSetmealId(setmealId);
        }

        //保存套餐數據至套餐菜品表
        setmealDishService.saveBatch(setmealDishes);
    }

    //查詢套餐
    @Override
    public SetmealDto getSetmealWithDish(Long id) {
        Setmeal setmeal=this.getById(id);

        SetmealDto setmealDto=new SetmealDto();

        BeanUtils.copyProperties(setmeal,setmealDto);

        List<SetmealDish> list = setmealDishService.findSetmealWithId(id);

        setmealDto.setSetmealDishes(list);

        return setmealDto;
    }

    //更新套餐
    @Override
    public void updateSetmeal(SetmealDto setmealDto) {
        //在套餐表中更新基本信息
        this.updateById(setmealDto);

        //更新套餐菜品表的數據(因頁面沒有回傳套餐菜品的id，故改為先刪除再新增)
        Long setmealId = setmealDto.getId();
        //刪除套餐菜品表中的數據
        setmealDishService.deleteBySetmealId(setmealId);
        //添加新的數據至套餐菜品表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        setmealDishService.saveBatch(setmealDishes);

    }

    //使用分類id找到套餐對象
    @Override
    public List<Setmeal> findSetmealWithCategoryId(Long categoryId) {
        return setmealMapper.findSetmealWithCategoryId(categoryId);
    }
}
