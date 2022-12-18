package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.pojo.*;
import com.example.service.AddressBookService;
import com.example.service.OrderDetailService;
import com.example.service.OrderService;
import com.example.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/order")
@Transactional
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;

    //建立訂單(去支付)
    @PostMapping("/submit")
    public R<String> orderCreate(@RequestBody Orders orders){
        log.info("訂單明細:{}",orders);

        //拿addressBookId找到addressBook對象
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);

        //注入和addressBook對象相關的訂單信息
        orders.setUserId(addressBook.getUserId());
        orders.setConsignee(addressBook.getConsignee());
        orders.setAddress(addressBook.getDetail());
        orders.setPhone(addressBook.getPhone());

        //設置訂單建立時間
        orders.setOrderTime(LocalDateTime.now());

        //注入其他信息
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setUserName(addressBook.getConsignee());
        orders.setStatus(1);

        //使用userId找到List<ShoppingCart>
        List<ShoppingCart> shoppingCartList = shoppingCartService.getShoppingCartByUserId(addressBook.getUserId());

        //從shoppingCart中計算amount*number注入orders
        BigDecimal total;
        BigDecimal total2=new BigDecimal(0);
        for (ShoppingCart shoppingCart : shoppingCartList) {
            Integer number = shoppingCart.getNumber();
            BigDecimal amount = shoppingCart.getAmount();
            total = BigDecimal.valueOf(number).multiply(amount);
            total2=total2.add(total);
        }

        //注入總金額
        orders.setAmount(total2);

        orderService.save(orders);

        //將購物車內的數據放入訂單明細表
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail=new OrderDetail();

            orderDetail.setName(shoppingCart.getName());
            orderDetail.setImage(shoppingCart.getImage());
            orderDetail.setOrderId(orders.getId());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());

            orderDetailService.save(orderDetail);
        }

        //清空購物車
        shoppingCartService.clean(addressBook.getUserId());

        return R.success("訂單建立成功!");
    }

    //查看訂單
    @GetMapping({"/userPage","/page"})
    public R<Page> page(int page, int pageSize, Long id, HttpSession session){
        //從Session中獲取用戶id
        Long userId = (Long) session.getAttribute("user");

        //建立分頁構造器
        Page<Orders> orderInfo=new Page();

        //建立條件構造器
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();

        //添加過濾條件
        queryWrapper.like(Objects.nonNull(id),Orders::getId,id);  //未生效

        //添加排序條件
        queryWrapper.orderByDesc(Orders::getOrderTime);

        //執行查詢
        orderService.page(orderInfo,queryWrapper);

        return R.success(orderInfo);
    }

}
