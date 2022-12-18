package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    //使用用戶id和菜品id查詢購物車內是否已有此項
    @Select("select * from shopping_cart where user_id=#{userId} and dish_id=#{dishId} and dish_flavor=#{dishFlavor}")
    public ShoppingCart findDishExist(@Param("userId") Long userId, @Param("dishId") Long dishId, @Param("dishFlavor") String dishFlavor);

    //使用用戶id和套餐id查詢購物車內是否已有此項
    @Select("select * from shopping_cart where user_id=#{userId} and setmeal_id=#{setmealId}")
    public ShoppingCart findSetmealExist(@Param("userId")Long userId, @Param("setmealId") Long setmealId);

    //使用用戶id和菜品id查詢購物車內的菜品口味(本方法未被使用)
    @Select("select dish_flavor from shopping_cart where user_id=#{userId} and dish_id=#{dishId}")
    public String findDishFlavor(@Param("userId") Long userId, @Param("dishId") Long dishId);

    //使用用戶id查詢購物車數據
    @Select("select * from shopping_cart where user_id=#{userId}")
    public List<ShoppingCart> getShoppingCartByUserId(Long userId);

    //清空購物車
    @Delete("delete from shopping_cart where user_id=#{userId}")
    public void clean(Long userId);

    //使用用戶id和菜品id刪除菜品
    @Delete("delete from shopping_cart where user_id=#{userId} and dish_id=#{dishId}")
    public void deleteDishes(@Param("userId") Long userId, @Param("dishId") Long dishId);

    //使用用戶id和套餐id刪除套餐
    @Delete("delete from shopping_cart where user_id=#{userId} and setmeal_id=#{setmealId}")
    public void deleteSetmeal(@Param("userId")Long userId, @Param("setmealId") Long setmealId);
}
