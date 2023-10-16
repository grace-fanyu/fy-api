package com.fanyu.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fanyu.fanyucommon.model.entity.User;
import com.fanyu.project.annotation.AuthCheck;
import com.fanyu.project.common.BaseResponse;
import com.fanyu.project.common.DeleteRequest;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.common.ResultUtils;
import com.fanyu.project.constant.UserConstant;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.exception.ThrowUtils;
import com.fanyu.project.model.dto.user.*;
import com.fanyu.project.model.vo.LoginUserVO;
import com.fanyu.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;



    // region 登录注册相关

    /**
     * 用户账号注册
     *
     * @param userRegisterRequest 用户账号注册信息
     * @return BaseResponse
     */
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }


        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户邮箱注册
     *
     * @param userRegisterRequest 用户邮箱注册信息
     * @return BaseResponse
     */
    @PostMapping("/email/register")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Long> userEmailRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String email = userRegisterRequest.getEmail();

        String code = userRegisterRequest.getCode();
        if (StringUtils.isAnyBlank(email,code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean verifyEmail = userService.verifyEmail(email, code);
        if (!verifyEmail){
            ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"验证码错误！");
        }

        long result = userService.userRegister(email);
        return ResultUtils.success(result);
    }
    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录信息
     * @param request request
     * @return BaseResponse
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 邮箱登录
     * @param userLoginRequest 用户登录信息
     * @param request HttpServletRequest
     * @return LoginUserVO
     */

    @PostMapping("/login/email")
    public BaseResponse<LoginUserVO> emailEmailLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String email = userLoginRequest.getEmail();
        String code = userLoginRequest.getCode();
        LoginUserVO loginUserVO = userService.userEmailLogin(email, code, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     *
     * @param emailRequest 邮箱
     * @param request HttpServletRequest
     * @return 邮箱发送状态
     */
    @PostMapping("/email")
    public BaseResponse<String> sendEmail(@RequestBody EmailRequest emailRequest ,HttpServletRequest request){
        if (emailRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String email = emailRequest.getEmail();
        System.out.println(email);

        String message = userService.sendEmail(email);
        return ResultUtils.success(message);
    }



    /**
     * 用户注销
     *
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }



    /**
     * 获取当前登录用户
     *
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    // endregion

    @GetMapping("/sign")
    public BaseResponse<Boolean> userSignInDaily(HttpServletRequest request){
        Boolean userBoolean =userService.userSignInDaily(request);

        return ResultUtils.success(userBoolean);
    }

    // region 增删改查

    /**
     * 创建用户
     *
     * @param userAddRequest UserAddRequest
     * @param request HttpServletRequest
     * @return  BaseResponse
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户
     *
     * @param deleteRequest DeleteRequest
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     *
     * @param userUpdateRequest UserUpdateRequest
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }



    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest UserQueryRequest
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
            HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }



    // endregion

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest UserUpdateMyRequest
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
