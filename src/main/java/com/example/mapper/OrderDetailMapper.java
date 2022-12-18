package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.pojo.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    //使用userId取得OrderDetail對象
    @Select("select * from order_detail where user_id=#{userId}")
    public List<OrderDetail> getOrderDetailsByUserId(Long userId);
}
