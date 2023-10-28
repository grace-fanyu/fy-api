package com.fanyu.project.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanyu.fanyucommon.model.entity.Settings;
import com.fanyu.fanyucommon.model.entity.User;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.constant.CommonConstant;
import com.fanyu.project.constant.UserConstant;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.exception.ThrowUtils;
import com.fanyu.project.mapper.UserMapper;
import com.fanyu.project.model.dto.user.ExchangeRequest;
import com.fanyu.project.model.dto.user.UserAddRequest;
import com.fanyu.project.model.dto.user.UserQueryRequest;
import com.fanyu.project.model.dto.user.UserUpdateMyRequest;
import com.fanyu.project.model.enums.UserRoleEnum;
import com.fanyu.project.model.vo.LoginUserVO;
import com.fanyu.project.model.vo.UserVO;
import com.fanyu.project.service.SettingsService;
import com.fanyu.project.service.UserService;
import com.fanyu.project.utils.SendMailUtil;
import com.fanyu.project.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.fanyu.project.constant.RedisConstants.SEND_EMAIL_KEY;
import static com.fanyu.project.constant.RedisConstants.SEND_EMAIL_TTL;

/**
 * 用户服务实现
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "fanyu";
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private SendMailUtil sendMailUtil;

    @Resource
    private SettingsService settingsService;


    @Override
    public long userRegister(String userName,String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userName,userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            //给用户分配调用接口的公钥和私钥ak,sk，保证复杂的同时要保证唯一
            String accessKey = DigestUtil.md5Hex(SALT+userAccount+ RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT+userAccount+ RandomUtil.randomNumbers(8));

            // 3. 插入数据

            User user = new User();
            user.setUserName(userName);
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            Settings settings = settingsService.getSettings();
            user.setUserAvatar(settings.getUserImg());

            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    /**
     * 用户注册
     *
     * @param email
     * @return
     */
    @Override
    public long userRegister(String userName,String email) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userName,email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (sendMailUtil.isNotEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式不对");
        }
        synchronized (email.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }

            //给用户分配调用接口的公钥和私钥ak,sk，保证复杂的同时要保证唯一
            String accessKey = DigestUtil.md5Hex(SALT+email+ RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT+email+ RandomUtil.randomNumbers(8));

            // 3. 插入数据

            User user = new User();
            user.setUserName(userName);
            user.setEmail(email);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            Settings settings = settingsService.getSettings();
            user.setUserAvatar(settings.getUserImg());

            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     * @return 发送成功的提示消息
     */
    @Override
    public String sendEmail(String email) {
        if (sendMailUtil.isNotEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未填写收件人邮箱");

        }
        // 定义Redis的key
        String key = SEND_EMAIL_KEY + email;

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String verifyCode = valueOperations.get(key);
        if (verifyCode != null) {
            return "验证码已发送至您的邮箱，请注意查收";
        }
        // 随机生成一个6位数字型的字符串
        String code = sendMailUtil.generateVerifyCode(6);
        // 邮件对象（邮件模板，根据自身业务修改）
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Fy-API 接口开放平台邮箱验证码");
        message.setText("尊敬的用户您好!\n感谢您使用 Fy-API。\n尊敬的: " + email + "您的校验验证码为: \n" + code + "\n有效期5分钟，请不要把验证码信息泄露给其他人,如非本人请勿操作");
        message.setTo(email);

        try {
            // 对方看到的发送人（发件人的邮箱，根据实际业务进行修改，一般填写的是企业邮箱）
            message.setFrom(new InternetAddress(MimeUtility.encodeText("Fy-API") + "<1143725742@qq.com>").toString());
            // 发送邮件
            javaMailSender.send(message);
            // 将生成的验证码存入Redis数据库中，并设置过期时间
            valueOperations.set(key, code, SEND_EMAIL_TTL, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "邮件发送出现异常，异常信息为"+ e.getMessage());
        }
        return "邮件发送成功";


    }

    /**
     * 验证邮箱验证码
     *
     * @param email 邮箱
     * @return 验证成功的提示消息
     */
    @Override
    public boolean verifyEmail(String email,String code) {
        if (sendMailUtil.isNotEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        if (StringUtils.isAnyBlank(code,email)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码为空");
        }
        // 定义Redis的key
        String key = SEND_EMAIL_KEY + email;
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String verifyCode = valueOperations.get(key);
        if (verifyCode == null ){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证码超时");
        }
        if ( !verifyCode.equals(code)){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证码错误");
        }
        return true;
    }

    /**
     * 用户平台账号登录
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求信息
     * @return LoginUserVO
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);

        return this.getLoginUserVO(user);
    }

    /**
     * 获取当前登录用户
     *
     * @param request HttpServletRequest
     * @return User
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
         //从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param request 请求
     * @return boolean
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 每日签到获取星琼
     *
     * @param request HttpServletRequest
     * @return Boolean
     */
    @Override
    public boolean userSignInDaily(HttpServletRequest request) {
        User user = this.getLoginUser(request);
        Settings settings = settingsService.getSettings();
        Long dailyStar = settings.getDailyStar();
        Long userStar = user.getUserStar();
        user.setUserStar(userStar+dailyStar);
        return this.updateById(user);
    }

    /**
     * 邮箱登录
     *
     * @param email   邮箱
     * @param code    验证码
     * @param request HttpServletRequest
     * @return LoginUserVO
     */
    @Override
    public LoginUserVO userEmailLogin(String email, String code, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(email, code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (sendMailUtil.isNotEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱错误");
        }

        boolean verifyEmail = this.verifyEmail(email, code);
        if (!verifyEmail){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        User user = this.baseMapper.selectOne(queryWrapper);

        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或邮箱错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    /**
     * 用户添加
     *
     * @param userAddRequest 用户创建信息
     */
    @Override
    public long addUser(UserAddRequest userAddRequest) {
        String userAccount = userAddRequest.getUserAccount();
        String userPassword = userAddRequest.getUserPassword();
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //给用户分配调用接口的公钥和私钥ak,sk，保证复杂的同时要保证唯一
        String accessKey = DigestUtil.md5Hex(SALT+userAccount+ RandomUtil.randomNumbers(5));
        String secretKey = DigestUtil.md5Hex(SALT+userAccount+ RandomUtil.randomNumbers(8));

        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        String userImg = settingsService.getSettings().getUserImg();
        user.setUserPassword(encryptPassword);
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        user.setUserAvatar(userImg);
        boolean result = this.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return user.getId();
    }

    /**
     * 更新自己的信息
     *
     * @param userUpdateMyRequest UserUpdateMyRequest
     * @return UserVO
     */
    @Override
    public UserVO updateMyUser(UserUpdateMyRequest userUpdateMyRequest) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        boolean result = this.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return this.getUserVO(user);
    }

    /**
     * 用户修改凭证
     *
     * @param user 用户信息
     * @return UserVO
     */
    @Override
    public UserVO updateVoucher(User user) {

        String userAccount = user.getUserAccount();
        String email = user.getEmail();
        if (StringUtils.isAllBlank(userAccount,email)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号异常");
        }
        String accessKey = user.getAccessKey();
        String secretKey = user.getSecretKey();
        if (userAccount != null ){
            //给用户分配调用接口的公钥和私钥ak,sk，保证复杂的同时要保证唯一
            accessKey = DigestUtil.md5Hex(SALT+userAccount+ RandomUtil.randomNumbers(5));
            secretKey = DigestUtil.md5Hex(SALT+userAccount+ RandomUtil.randomNumbers(8));
        }
        if (userAccount == null & email !=null){
            accessKey = DigestUtil.md5Hex(SALT+email+ RandomUtil.randomNumbers(5));
            secretKey = DigestUtil.md5Hex(SALT+email+ RandomUtil.randomNumbers(8));

        }
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        boolean result = this.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);

        return this.getUserVO(user);
    }

    /**
     * 钻石兑换星琼
     *
     * @param exchangeStarRequest 兑换信息
     * @return UserVO
     */
    @Override
    public UserVO exchangeStar(ExchangeRequest exchangeStarRequest) {
        User user = this.getById(exchangeStarRequest.getId());
        Settings settings = settingsService.getSettings();
        Long starDiamond = settings.getStarDiamond();
        Long userDiamond = exchangeStarRequest.getUserDiamond();

        Long userStar = (userDiamond*starDiamond)+user.getUserStar();
        userDiamond = user.getUserDiamond() - userDiamond;
        user.setUserStar(userStar);
        user.setUserDiamond(userDiamond);
        boolean result = this.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return this.getUserVO(user);
    }

    /**
     * 充值钻石
     *
     * @param exchangeRequest 充值信息
     * @return 用户脱敏信息
     */
    @Override
    public UserVO rechargeDiamond(ExchangeRequest exchangeRequest) {
        User user = this.getById(exchangeRequest.getId());
        user.setUserDiamond(user.getUserDiamond()+exchangeRequest.getUserDiamond());
        boolean result = this.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return this.getUserVO(user);
    }

    /**
     * 用户注销
     * @param request HttpServletRequest
     * @return boolean
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(unionId), "unionId", unionId);
        queryWrapper.eq(StringUtils.isNotBlank(mpOpenId), "mpOpenId", mpOpenId);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


}
