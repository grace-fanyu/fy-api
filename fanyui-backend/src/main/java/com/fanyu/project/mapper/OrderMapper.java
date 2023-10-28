package com.fanyu.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fanyu.fanyucommon.model.entity.Order;
import com.fanyu.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.fanyu.project.model.dto.order.OrderQueryRequest;
import com.fanyu.project.model.vo.InterfaceInfoListVO;
import com.fanyu.project.model.vo.OrderVO;

import java.util.List;

/**
 * 用户订单信息表 Mapper
 * @author 凡雨
 */
public interface OrderMapper extends BaseMapper<Order> {

    List<OrderVO> listOrder(OrderQueryRequest orderQueryRequest);


}




