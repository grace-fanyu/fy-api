package com.fanyu.fanyucommon.service;


import com.fanyu.fanyucommon.model.entity.User;

/**
 * 内部用户服务
 * @author 凡雨
 */
public interface InnerUserService {

    /**
     * 数据库中查是否已分配给用户秘钥（accessKey）
     * @param accessKey 用户密钥
     * @return 查询用户信息
     */
    User getInvokeUser(String accessKey);

}
