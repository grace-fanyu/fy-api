package com.fanyu.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanyu.fanyucommon.model.entity.Order;
import com.fanyu.project.mapper.OrderMapper;
import com.fanyu.project.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * 用户订单信息
 *
 * @author 凡雨
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

}




