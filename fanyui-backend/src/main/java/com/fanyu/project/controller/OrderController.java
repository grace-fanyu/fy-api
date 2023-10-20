package com.fanyu.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanyu.fanyucommon.model.entity.Order;
import com.fanyu.fanyucommon.model.entity.User;
import com.fanyu.project.annotation.AuthCheck;
import com.fanyu.project.common.BaseResponse;
import com.fanyu.project.common.DeleteRequest;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.common.ResultUtils;
import com.fanyu.project.constant.UserConstant;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.exception.ThrowUtils;
import com.fanyu.project.model.dto.order.OrderAddRequest;
import com.fanyu.project.model.dto.order.OrderCreateRequest;
import com.fanyu.project.model.dto.order.OrderQueryRequest;
import com.fanyu.project.model.dto.order.OrderUpdateRequest;
import com.fanyu.project.model.vo.OrderVO;
import com.fanyu.project.service.OrderService;
import com.fanyu.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单接口
 *
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;
    @Resource
    private UserService userService;



    // region 登录注册相关


    /**
     * 用户下订单买接口调用次数
     *
     * @param orderCreateRequest OrderAddRequest
     * @param request HttpServletRequest
     * @return  BaseResponse
     */
    @PostMapping("/create")
    public BaseResponse<Long> createOrder(@RequestBody OrderCreateRequest orderCreateRequest, HttpServletRequest request) {
        if (orderCreateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        orderCreateRequest.setUserId(user.getId());
        long createOrder = orderService.createOrder(orderCreateRequest);
        return ResultUtils.success(createOrder);
    }






    /**
     * 订单取消
     *
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> orderLogout(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = orderService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }



    /**
     * 根据订单 id 获取当前订单详细信息
     *
     * @param id 订单id
     * @return BaseResponse
     */
    @GetMapping("/get/order")
    public BaseResponse<OrderVO> getLoginOrderById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Order order = orderService.getById(id);
        return ResultUtils.success(orderService.getOrderVO(order));
    }
    /**
     * 分页获取列表
     *
     * @param orderQueryRequest InterfaceInfoQueryRequest
     * @param request                   HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/list/page")
    public BaseResponse<List<OrderVO>> listOrderByPage(@RequestBody OrderQueryRequest orderQueryRequest, HttpServletRequest request) {
        if (orderQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<OrderVO> orderVOList = orderService.orderVOPage(orderQueryRequest,request);

        return ResultUtils.success(orderVOList);
    }


    // region 增删改查

    /**
     * 创建订单
     *
     * @param orderAddRequest OrderAddRequest
     * @param request HttpServletRequest
     * @return  BaseResponse
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addOrder(@RequestBody OrderAddRequest orderAddRequest, HttpServletRequest request) {
        if (orderAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        orderAddRequest.setUserId(user.getId());
        long addOrder = orderService.addOrder(orderAddRequest);
        return ResultUtils.success(addOrder);
    }

    /**
     * 删除订单
     *
     * @param deleteRequest DeleteRequest
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteOrder(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = orderService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新订单
     *
     * @param orderUpdateRequest OrderUpdateRequest
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateOrder(@RequestBody OrderUpdateRequest orderUpdateRequest,
                                            HttpServletRequest request) {
        if (orderUpdateRequest == null || orderUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Order order = new Order();
        BeanUtils.copyProperties(orderUpdateRequest, order);
        boolean result = orderService.updateById(order);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取订单（仅管理员）
     *
     * @param id 订单id
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Order> getOrderById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Order order = orderService.getById(id);
        ThrowUtils.throwIf(order == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(order);
    }



    /**
     * 分页获取订单列表（仅管理员）
     *
     * @param orderQueryRequest OrderQueryRequest
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<Order>> listOrderBy(@RequestBody OrderQueryRequest orderQueryRequest,
                                                   HttpServletRequest request) {
        long current = orderQueryRequest.getCurrent();
        long size = orderQueryRequest.getPageSize();
        Page<Order> orderPage = orderService.page(new Page<>(current, size),
                orderService.getQueryWrapper(orderQueryRequest));
        return ResultUtils.success(orderPage);
    }




}
