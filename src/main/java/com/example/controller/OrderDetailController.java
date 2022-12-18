package com.example.controller;

import com.example.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orderdetail")
@Transactional
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;
}
