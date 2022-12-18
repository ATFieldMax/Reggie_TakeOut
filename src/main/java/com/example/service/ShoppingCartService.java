package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    //使用用戶id和菜品id查詢購物車內是否已有此項
    public ShoppingCart findDishExist(Long userId,Long dishId,String dishFlavor);

    //使用用戶id和套餐id查詢購物車內是否已有此項
    public ShoppingCart findSetmealExist(Long userId,Long setmealId);

    //使用用戶id和菜品id查詢購物車內的菜品口味(本方法未被使用)
    public String findDishFlavor(Long userId,Long dishId);

    //使用用戶id查詢購物車數據
    public List<ShoppingCart> getShoppingCartByUserId(Long userId);

    //清空購物車
    public void clean(Long userId);

    //使用用戶id和菜品id刪除菜品
    public void deleteDishes(@Param("userId") Long userId, @Param("dishId") Long dishId);

    //使用用戶id和套餐id刪除套餐
    public void deleteSetmeal(@Param("userId")Long userId, @Param("setmealId") Long setmealId);
}
