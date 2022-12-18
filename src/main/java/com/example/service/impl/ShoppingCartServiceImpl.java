package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.ShoppingCartMapper;
import com.example.pojo.ShoppingCart;
import com.example.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    //使用用戶id和菜品id查詢購物車內是否已有此項
    @Override
    public ShoppingCart findDishExist(Long userId, Long dishId, String dishFlavor) {
        return shoppingCartMapper.findDishExist(userId,dishId,dishFlavor);
    }

    //使用用戶id和套餐id查詢購物車內是否已有此項
    @Override
    public ShoppingCart findSetmealExist(Long userId, Long setmealId) {
        return shoppingCartMapper.findSetmealExist(userId,setmealId);
    }

    //使用用戶id和菜品id查詢購物車內的菜品口味(本方法未被使用)
    @Override
    public String findDishFlavor(Long userId, Long dishId) {
        return shoppingCartMapper.findDishFlavor(userId,dishId);
    }

    //使用用戶id查詢購物車數據
    @Override
    public List<ShoppingCart> getShoppingCartByUserId(Long userId) {
        return shoppingCartMapper.getShoppingCartByUserId(userId);
    }

    //清空購物車
    @Override
    public void clean(Long userId) {
        shoppingCartMapper.clean(userId);
    }

    //使用用戶id和菜品id刪除菜品
    @Override
    public void deleteDishes(Long userId, Long dishId) {
        shoppingCartMapper.deleteDishes(userId,dishId);
    }

    //使用用戶id和套餐id刪除套餐
    @Override
    public void deleteSetmeal(Long userId, Long setmealId) {
        shoppingCartMapper.deleteSetmeal(userId,setmealId);
    }
}
