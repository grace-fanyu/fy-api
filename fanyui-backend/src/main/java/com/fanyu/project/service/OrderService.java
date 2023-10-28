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



    /**
     * 获取脱敏订单
     * @param order 订单
     * @return OrderVO
     */
    OrderVO getOrderVO(Order order);

    /**
     * 管理员添加订单
     * @param orderAddRequest OrderAddRequest
     * @return 订单id
     */
    long addOrder(OrderAddRequest orderAddRequest);

    /**
     * 查询订单
     * @param orderQueryRequest OrderQueryRequest
     * @return QueryWrapper<Order>
     */
    QueryWrapper<Order> getQueryWrapper(OrderQueryRequest orderQueryRequest);

    /**
     * 添加订单
     * @param orderCreateRequest OrderCreateRequest
     * @return 订单id
     */
    long createOrder(OrderCreateRequest orderCreateRequest);


    /**
     * 订单列表
     * @param orderQueryRequest OrderQueryRequest
     * @param request HttpServletRequest
     * @return 订单列表
     */
    List<OrderVO> orderVOPage(OrderQueryRequest orderQueryRequest, HttpServletRequest request);

    /**
     * 订单确认
     * @param id 订单id
     * @return OrderVO
     */
    OrderVO confirmOrderById(long id);
}
