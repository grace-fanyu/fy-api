package com.fanyu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanyu.fanyucommon.model.entity.User;
import com.fanyu.fanyucommon.model.entity.UserInterfaceInfo;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.exception.ThrowUtils;
import com.fanyu.project.mapper.UserInterfaceInfoMapper;
import com.fanyu.project.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.fanyu.project.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.fanyu.project.model.vo.UserInterfaceInfoVO;
import com.fanyu.project.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.List;

@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {

    @Resource
    InterfaceInfoServiceImpl interfaceInfoService;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();


        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(interfaceInfoId == null,ErrorCode.PARAMS_ERROR);
            if (interfaceInfoId <= 0 || userId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        // 有参数则校验
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于 0");
        }
        if (userInterfaceInfo.getTotalNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "总次数不能小于 0");
        }

    }

    /**
     * 接口调用剩余次数查询
     *
     * @param InterfaceId 接口调用id
     * @param userId 用户id
     * @return Integer
     */
    @Override
    public Integer getUserInterfaceInfoLeftNum(Long InterfaceId,Long userId) {
        if (InterfaceId <= 0 ||userId <=0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<UserInterfaceInfo> userInterfaceInfo = new QueryWrapper<>();
        userInterfaceInfo.eq("userId",userId);
        userInterfaceInfo.eq("InterfaceId",InterfaceId);
        return this.getOne(userInterfaceInfo).getLeftNum();
    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        //判空
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
        }
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userId",userId);
        updateWrapper.eq("interfaceInfoId",interfaceInfoId);
        updateWrapper.setSql("leftNum = leftNum - 1 , totalNum = totalNum + 1");
        return this.update(updateWrapper);
    }

    /**
     * 获取接口调用列表
     *
     * @param userInterfaceInfoQueryRequest UserInterfaceInfoQueryRequest
     * @param request HttpServletRequest
     * @return List<UserInterfaceInfoVO>
     */
    @Override
    public List<UserInterfaceInfoVO> userInterfaceVOPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest, HttpServletRequest request) {
        return null;
    }

    /**
     * 接口调用次数修改 TODO
     *
     * @param userInterfaceInfoUpdateRequest 接口调用更新请求
     * @param request                        请求参数
     * @return 是否更新成功
     */
    @Override
    public Boolean userUpdateUserInterfaceInfo(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest, HttpServletRequest request) {
        return null;
    }
}
