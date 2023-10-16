package com.fanyu.project.service.impl.inner;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanyu.fanyucommon.model.entity.UserInterfaceInfo;
import com.fanyu.fanyucommon.service.InnerUserInterfaceInfoService;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.mapper.UserInterfaceInfoMapper;
import com.fanyu.project.service.impl.UserInterfaceInfoServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import javax.management.Query;

@DubboService(version = "1.0.1")
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    UserInterfaceInfoServiceImpl userInterfaceInfoService;
    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId 接口id
     * @param userId          用户id
     * @return 返回是否成功被调用
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        queryWrapper.eq("interfaceInfoId",interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(queryWrapper);
        if (userInterfaceInfo.getLeftNum() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"剩余调用次数不足");
        }
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }




}
