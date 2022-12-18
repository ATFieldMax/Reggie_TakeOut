package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    //使用userId取得OrderDetail對象
    public List<OrderDetail> getOrderDetailsByUserId(Long userId);
}
