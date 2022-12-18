package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.OrderDetailMapper;
import com.example.pojo.OrderDetail;
import com.example.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    //使用userId取得OrderDetail對象
    @Override
    public List<OrderDetail> getOrderDetailsByUserId(Long userId) {
        return orderDetailMapper.getOrderDetailsByUserId(userId);
    }
}
