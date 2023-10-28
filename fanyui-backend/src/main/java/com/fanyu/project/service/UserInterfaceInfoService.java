package com.fanyu.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fanyu.fanyucommon.model.entity.UserInterfaceInfo;
import com.fanyu.project.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.fanyu.project.model.vo.UserInterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接口调用信息
 * @author 凡雨
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 接口调用信息检测
     * @param userInterfaceInfo 接口调用
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 接口调用剩余次数查询
     * @param InterfaceId 接口id
     * @param userId 用户id
     * @return  Integer
     */
    Integer getUserInterfaceInfoLeftNum(Long InterfaceId ,Long userId);

    /**
     * 调用接口次数统计
     * @param interfaceInfoId 接口id
     * @param userId 用户id
     * @return 是否成功
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取接口调用列表
     * @param userInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest
     * @param request HttpServletRequest
     * @return 信息列表
     */
    List<UserInterfaceInfoVO> userInterfaceVOPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request);



    /**
     * 创建用户调用信息
     * @param userInterfaceInfo 创建信息
     * @return 接口调用信息id
     */
    Long createUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo);
}
