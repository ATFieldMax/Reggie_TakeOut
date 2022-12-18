package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pojo.User;

public interface UserService extends IService<User> {
    //使用手機號查詢用戶
    public User selectUserByPhone(String phone);
}
