package com.fanyu.project.service.impl.inner;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanyu.fanyucommon.model.entity.User;
import com.fanyu.fanyucommon.service.InnerUserService;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 用户服务实现
 *
 */
@DubboService(version = "1.0.1")
public class InnerUserServiceImpl  implements InnerUserService {

    @Resource
    UserMapper userMapper;

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     *
     * @param accessKey 用户密钥
     * @return 查询用户信息
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey",accessKey);
        return userMapper.selectOne(userQueryWrapper);
    }
}
