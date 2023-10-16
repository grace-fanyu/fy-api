package com.fanyu.fanyucommon.service;



/**
 * 内部用户调用接口信息服务
 * @author 凡雨
 */
public interface InnerUserInterfaceInfoService {



    /**
     * 调用接口次数+1
     * @param interfaceInfoId 接口id
     * @param userId 用户id
     * @return 返回是否成功被调用
     */
    boolean invokeCount(long interfaceInfoId, long userId);

}
