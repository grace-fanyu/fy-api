package com.fanyu.project.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fanyu.fanyucommon.model.entity.Order;
import com.fanyu.project.model.dto.order.OrderAddRequest;
import com.fanyu.project.model.dto.order.OrderCreateRequest;
import com.fanyu.project.model.dto.order.OrderQueryRequest;
import com.fanyu.project.model.vo.OrderVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户订单信息
* @author 凡雨
*/
public interface OrderService extends IService<Order> {

    Order getOrder(HttpServletRequest request);

    OrderVO getOrderVO(Order order);

    long addOrder(OrderAddRequest orderAddRequest);

    QueryWrapper<Order> getQueryWrapper(OrderQueryRequest orderQueryRequest);

    long createOrder(OrderCreateRequest orderCreateRequest);

    boolean orderLogout(HttpServletRequest request);

    List<OrderVO> orderVOPage(OrderQueryRequest orderQueryRequest, HttpServletRequest request);
}
