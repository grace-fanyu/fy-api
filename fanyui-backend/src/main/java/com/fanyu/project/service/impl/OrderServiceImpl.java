package com.fanyu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanyu.fanyucommon.model.entity.InterfaceInfo;
import com.fanyu.fanyucommon.model.entity.Order;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.constant.CommonConstant;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.exception.ThrowUtils;
import com.fanyu.project.mapper.OrderMapper;
import com.fanyu.project.model.dto.order.OrderAddRequest;
import com.fanyu.project.model.dto.order.OrderCreateRequest;
import com.fanyu.project.model.dto.order.OrderQueryRequest;
import com.fanyu.project.model.vo.OrderVO;
import com.fanyu.project.service.InterfaceInfoService;
import com.fanyu.project.service.OrderService;
import com.fanyu.project.service.UserInterfaceInfoService;
import com.fanyu.project.service.UserService;
import com.fanyu.project.utils.SqlUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户订单信息
 *
 * @author 凡雨
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {


    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserService userService;
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 获取脱敏订单
     *
     * @param order 订单
     * @return OrderVO
     */
    @Override
    public OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        Long userId = order.getUserId();
        Long interfaceInfoId = order.getInterfaceInfoId();
        String userName = userService.getById(userId).getUserName();
        String interfaceName = interfaceInfoService.getById(interfaceInfoId).getName();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setInterfaceName(interfaceName);
        orderVO.setUserName(userName);
        return orderVO;
    }

    /**
     * 管理员添加订单
     *
     * @param orderAddRequest OrderAddRequest
     * @return 订单id
     */
    @Override
    public long addOrder(OrderAddRequest orderAddRequest) {
        Long interfaceInfoId = orderAddRequest.getInterfaceInfoId();
        // 1.检验参数
        if (orderAddRequest.getTotalNum() < 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"购买次数不能小于100！");
        }
        if (orderAddRequest.getPriceStar() <= 0 & orderAddRequest.getPriceDiamond() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"花费有错误！");
        }
        // 2.查询接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 3.创建订单到库中
        Order order = new Order();
        BeanUtils.copyProperties(orderAddRequest, order);
        boolean result = this.save(order);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return order.getId();
    }

    /**
     * 查询订单
     *
     * @param orderQueryRequest OrderQueryRequest
     * @return QueryWrapper<Order>
     */
    @Override
    public QueryWrapper<Order> getQueryWrapper(OrderQueryRequest orderQueryRequest) {
        if (orderQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = orderQueryRequest.getId();
        Long userId = orderQueryRequest.getUserId();
        Long interfaceInfoId = orderQueryRequest.getInterfaceInfoId();
        Integer totalNum = orderQueryRequest.getTotalNum();
        Long priceStar = orderQueryRequest.getPriceStar();
        Long priceDiamond = orderQueryRequest.getPriceDiamond();
        String sortField = orderQueryRequest.getSortField();
        String sortOrder = orderQueryRequest.getSortOrder();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(interfaceInfoId != null, "interfaceInfoId", interfaceInfoId);
        queryWrapper.eq(totalNum != null, "totalNum", totalNum);
        queryWrapper.eq(priceStar != null, "priceStar", priceStar);
        queryWrapper.eq(priceDiamond != null, "priceDiamond", priceDiamond);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 添加订单
     *
     * @param orderCreate 订单请求信息
     * @return 订单id
     */
    @Override
    public long createOrder(OrderCreateRequest orderCreate) {
        // 1.检验参数
        if (orderCreate.getTotalNum() < 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"购买次数不能小于100！");
        }
        if (orderCreate.getPriceStar() <= 0 & orderCreate.getPriceDiamond() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"花费有错误！");
        }
        // 2.查询接口是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(orderCreate.getInterfaceInfoId());
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 3.创建订单到库中
        Order order = new Order();
        BeanUtils.copyProperties(orderCreate, order);
        boolean result = this.save(order);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return order.getId();
    }



    /**
     * 订单列表
     *
     * @param orderQueryRequest OrderQueryRequest
     * @param request           HttpServletRequest
     * @return 订单列表
     */
    @Override
    public List<OrderVO> orderVOPage(OrderQueryRequest orderQueryRequest, HttpServletRequest request) {
        if (orderQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return this.baseMapper.listOrder(orderQueryRequest);
    }

    /**
     * 订单确认
     *
     * @param id 订单id
     * @return OrderVO
     */
    @Override
    public OrderVO confirmOrderById(long id) {
        Order order = this.getById(id);
        order.setStatus(1);

        return null;
    }


}