package com.fanyu.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fanyu.fanyucommon.model.entity.Order;
import com.fanyu.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.fanyu.project.model.dto.order.OrderQueryRequest;
import com.fanyu.project.model.vo.InterfaceInfoListVO;
import com.fanyu.project.model.vo.OrderVO;

import java.util.List;

/**
* @author lison
* @description 针对表【order(用户订单信息表)】的数据库操作Mapper
* @createDate 2023-10-11 19:55:52
* @Entity generator.domain.Order
*/
public interface OrderMapper extends BaseMapper<Order> {

    List<OrderVO> listOrder(OrderQueryRequest orderQueryRequest);


}




