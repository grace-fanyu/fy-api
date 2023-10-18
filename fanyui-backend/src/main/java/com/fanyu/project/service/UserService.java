package com.fanyu.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fanyu.fanyucommon.model.entity.User;
import com.fanyu.project.model.dto.user.UserAddRequest;
import com.fanyu.project.model.dto.user.UserQueryRequest;
import com.fanyu.project.model.vo.LoginUserVO;
import com.fanyu.project.model.vo.UserVO;

import java.util.List;
import javax.servlet.http.HttpServletRequest;


/**
 * 用户业务接口
 *
 * @author 凡雨
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户注册
     * @param email
     * @return
     */
    long userRegister(String email);

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 发送成功的提示消息
     */
    String sendEmail(String email);

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 是否验证成功
     */
    boolean verifyEmail(String email, String code);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求信息
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 获取当前登录用户
     *
     * @param request 请求信息
     * @return User
     */
    User getLoginUser(HttpServletRequest request);


    /**
     * 是否为管理员
     *
     * @param request 请求信息
     * @return boolean
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user User
     * @return boolean
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request 请求信息
     * @return boolean
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return LoginUserVO
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user 用户信息
     * @return UserVO
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList List<User>
     * @return List<UserVO>
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest UserQueryRequest
     * @return QueryWrapper<User>
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 每日签到获取星琼
     *
     * @param request HttpServletRequest
     * @return Boolean
     */
    boolean userSignInDaily(HttpServletRequest request);

    /**
     * 邮箱登录
     *
     * @param email   邮箱
     * @param code    验证码
     * @param request HttpServletRequest
     * @return LoginUserVO
     */
    LoginUserVO userEmailLogin(String email, String code, HttpServletRequest request);


    /**
     * 用户添加
     * @param userAddRequest 用户创建信息
     */
    long addUser(UserAddRequest userAddRequest);
}
