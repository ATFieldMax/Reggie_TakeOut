package com.example.controller;

import com.example.common.R;
import com.example.pojo.ShoppingCart;
import com.example.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
@Transactional
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        //從Session中獲取用戶id
        Long userId = (Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);

        shoppingCart.setCreateTime(LocalDateTime.now());

        log.info("添加至購物車:{}",shoppingCart);

        //查詢當前菜品或套餐是否已在購物車中
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        if(dishId != null){
            //添加到購物車的是菜品
            //使用用戶id、菜品id和口味查詢購物車內是否已有此項
            String dishFlavor = shoppingCart.getDishFlavor();
            ShoppingCart dishCart = shoppingCartService.findDishExist(userId, dishId,dishFlavor);
            if(dishCart != null){
                //菜品已經存在，再加一份
                Integer number = dishCart.getNumber();
                number++;
                shoppingCart.setNumber(number);
                log.info(shoppingCart.toString());

                Long id = dishCart.getId();
                shoppingCart.setId(id);
                shoppingCartService.updateById(shoppingCart);

                return R.success(shoppingCart);
            }
        }

        if(setmealId != null){
            //添加到購物車的是套餐
            //使用用戶id和套餐id查詢購物車內是否已有此項
            ShoppingCart setmealCart = shoppingCartService.findSetmealExist(userId, setmealId);
            if(setmealCart != null){
                //套餐已經存在，再加一份
                Integer number = setmealCart.getNumber();
                number++;
                shoppingCart.setNumber(number);
                Long id = setmealCart.getId();
                shoppingCart.setId(id);
                shoppingCartService.updateById(shoppingCart);

                return R.success(shoppingCart);
            }
        }

        shoppingCartService.save(shoppingCart);

        return R.success(shoppingCart);
    }

    //獲取購物車內的商品
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(HttpSession session){
        //從Session中獲取用戶id
        Long userId = (Long) session.getAttribute("user");
        //查詢對應用戶的購物車數據
        List<ShoppingCart> shoppingCartList = shoppingCartService.getShoppingCartByUserId(userId);

        return R.success(shoppingCartList);
    }

    //刪除購物車內的商品
    @PostMapping("/sub")
    public R<String> delete(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        //從Session中獲取用戶id
        Long userId = (Long) session.getAttribute("user");
        shoppingCart.setUserId(userId);

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        if(dishId != null){
            //使用用戶id和菜品id刪除菜品
            shoppingCartService.deleteDishes(userId,dishId);
        }
        if(setmealId != null){
            //使用用戶id和套餐id刪除套餐
            shoppingCartService.deleteSetmeal(userId,setmealId);
        }

        return R.success("刪除成功!");
    }

    //清空購物車
    @DeleteMapping("/clean")
    public R<String> clean(HttpSession session){
        //從Session中獲取用戶id
        Long userId = (Long) session.getAttribute("user");
        //清空對應用戶的購物車
        shoppingCartService.clean(userId);

        return R.success("購物車已清空!");
    }
}
