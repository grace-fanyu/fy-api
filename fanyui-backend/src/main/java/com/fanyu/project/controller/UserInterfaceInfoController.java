package com.fanyu.project.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanyu.fanyucommon.model.entity.User;
import com.fanyu.fanyucommon.model.entity.UserInterfaceInfo;
import com.fanyu.project.annotation.AuthCheck;
import com.fanyu.project.common.BaseResponse;
import com.fanyu.project.common.DeleteRequest;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.common.ResultUtils;
import com.fanyu.project.constant.CommonConstant;
import com.fanyu.project.constant.UserConstant;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.fanyu.project.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.fanyu.project.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.fanyu.project.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.fanyu.project.model.vo.InterfaceInfoVO;
import com.fanyu.project.model.vo.UserInterfaceInfoVO;
import com.fanyu.project.service.UserInterfaceInfoService;
import com.fanyu.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户调用接口关系
 */
@RestController
@RequestMapping("/userUserInterfaceInfo")
@Slf4j
public class UserInterfaceInfoController {


    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Resource
    private UserService userService;

    /**
     * 接口调用剩余次数查询
     *
     * @param InterfaceId 接口id
     * @param request     HttpServletRequest
     * @return 剩余次数
     */
    @GetMapping("/get/left")
    public BaseResponse<Integer> getUserInterfaceInfoLeftNum(Long InterfaceId, HttpServletRequest request) {
        if (InterfaceId == null || InterfaceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        Integer leftNum = userInterfaceInfoService.getUserInterfaceInfoLeftNum(InterfaceId, userId);
        return ResultUtils.success(leftNum);
    }




    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest
     * @param request                       HttpServletRequest
     * @return BaseResponse
     */
    @GetMapping("/list/page")
    public BaseResponse<List<UserInterfaceInfoVO>> listInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<UserInterfaceInfoVO> userInterfaceInfoVO = userInterfaceInfoService.userInterfaceVOPage(userInterfaceInfoQueryRequest, request);

        return ResultUtils.success(userInterfaceInfoVO);
    }
    // 接口调用信息根据id查询

    /**
     * 根据 id 获取
     *
     * @param id 调用接口id
     * @return 获取到的用户调用接口信息
     */
    @GetMapping("/user/get")
    public BaseResponse<UserInterfaceInfoVO> getUserInterfaceInfo(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        UserInterfaceInfoVO userInterfaceInfoVO = new UserInterfaceInfoVO();
        BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoVO);
        return ResultUtils.success(userInterfaceInfoVO);
    }
    // region 增删改查

    /**
     * 接口调用信息创建
     *
     * @param userInterfaceInfoAddRequest UserInterfaceInfoAddRequest请求体
     * @param request                     请求参数
     * @return 用户调用信息id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        // 校验
        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(loginUser.getId());
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, true);
        boolean result = userInterfaceInfoService.save(userInterfaceInfo);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        long newUserInterfaceInfoId = userInterfaceInfo.getId();
        return ResultUtils.success(newUserInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest 删除id
     * @param request       请求参数
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断用户调用接口是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = userInterfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新
     *
     * @param userInterfaceInfoUpdateRequest 接口调用更新请求
     * @param request                        请求参数
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest,
                                                         HttpServletRequest request) {
        if (userInterfaceInfoUpdateRequest == null || userInterfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        // 参数校验
        userInterfaceInfoService.validUserInterfaceInfo(userInterfaceInfo, false);
        User user = userService.getLoginUser(request);
        long id = userInterfaceInfoUpdateRequest.getId();
        // 判断是否存在
        UserInterfaceInfo oldUserInterfaceInfo = userInterfaceInfoService.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可修改
        if (!oldUserInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = userInterfaceInfoService.updateById(userInterfaceInfo);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id 调用接口id
     * @return 获取到的用户调用接口信息
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<UserInterfaceInfo> getUserInterfaceInfoById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }



    /**
     * 分页获取列表
     *
     * @param userInterfaceInfoQueryRequest 分页查询条件
     * @param request                       请求参数
     * @return 分页调用接口信息列表
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserInterfaceInfo>> listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        if (userInterfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfoQuery);
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long size = userInterfaceInfoQueryRequest.getPageSize();
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();

        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfoQuery);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceInfoPage = userInterfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(userInterfaceInfoPage);
    }
    // endregion


}