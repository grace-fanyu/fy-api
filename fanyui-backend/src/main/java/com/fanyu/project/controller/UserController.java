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
import com.fanyu.project.model.dto.order.OrderUpdateRequest;
import com.fanyu.project.model.dto.user.*;
import com.fanyu.project.model.vo.LoginUserVO;
import com.fanyu.project.model.vo.OrderVO;
import com.fanyu.project.model.vo.UserVO;
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


    /**
     * 用户平台账号注册
     * @param userRegisterRequest 用户账号注册信息
     * @return BaseResponse
     */
    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userName = userRegisterRequest.getUserName();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userName,userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long result = userService.userRegister(userName,userAccount, userPassword, checkPassword);
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
        String userName = userRegisterRequest.getUserName();
        String email = userRegisterRequest.getEmail();

        String code = userRegisterRequest.getCode();
        if (StringUtils.isAnyBlank(userName,email,code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean verifyEmail = userService.verifyEmail(email, code);
        if (!verifyEmail){
            ResultUtils.error(ErrorCode.NOT_FOUND_ERROR,"验证码错误！");
        }

        long result = userService.userRegister(userName,email);
        return ResultUtils.success(result);
    }

    /**
     * 用户平台账号登录
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
    public BaseResponse<LoginUserVO> emailLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String email = userLoginRequest.getEmail();
        String code = userLoginRequest.getCode();
        LoginUserVO loginUserVO = userService.userEmailLogin(email, code, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 根据邮箱信息发送验证码
     * @param email 邮箱
     * @param request HttpServletRequest
     * @return 邮箱发送状态
     */
    @GetMapping("/email")
    public BaseResponse<String> sendEmail(String email ,HttpServletRequest request){
        if (StringUtils.isAnyBlank(email)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //String email = emailRequest.getEmail();
        //System.out.println(email);

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
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getUserVO(user));
    }

    // endregion

    /**
     * 每日签到
     * @param request HttpServletRequest
     * @return BaseResponse
     */
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
        long addUser = userService.addUser(userAddRequest);
        return ResultUtils.success(addUser);
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
     * @param id 用户id
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
    public BaseResponse<UserVO> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
            HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (userUpdateMyRequest.getId() <= 0){
            User user = userService.getLoginUser(request);
            userUpdateMyRequest.setId(user.getId());
        }
         UserVO userVO= userService.updateMyUser(userUpdateMyRequest);

        return ResultUtils.success(userVO);
    }
    /**
     * 更新个人凭证
     *
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/update/voucher")
    public BaseResponse<UserVO> updateVoucher(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        User user = userService.getLoginUser(request);


        UserVO userVO= userService.updateVoucher(user);

        return ResultUtils.success(userVO);
    }

    /**
     * 兑换星琼
     * @param exchangeStarRequest 兑换信息
     * @param request 请求
     * @return  UserVO
     */
    @PostMapping("/exchange")
    public BaseResponse<UserVO> ExchangeStar(@RequestBody ExchangeRequest exchangeStarRequest, HttpServletRequest request) {
        if (exchangeStarRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        exchangeStarRequest.setId(user.getId());
        UserVO userVO = userService.exchangeStar(exchangeStarRequest);
        return ResultUtils.success(userVO);
    }
    //topUp

    /**
     * 充值钻石
     * @param exchangeRequest  充值信息
     * @param request HttpServletRequest
     * @return UserVO
     */
    @PostMapping("/recharge")
    public BaseResponse<UserVO> RechargeDiamond(@RequestBody ExchangeRequest exchangeRequest, HttpServletRequest request) {
        if (exchangeRequest == null|| exchangeRequest.getUserDiamond() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        exchangeRequest.setId(user.getId());
        UserVO userVO = userService.rechargeDiamond(exchangeRequest);
        return ResultUtils.success(userVO);
    }
}
