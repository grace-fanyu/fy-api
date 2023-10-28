package com.fanyu.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fanyu.fanyucommon.model.entity.UserInterfaceInfo;
import com.fanyu.project.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.fanyu.project.model.vo.UserInterfaceInfoVO;

import java.util.List;
/**
 * 用户接口调用信息数据库操作
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

    List<UserInterfaceInfoVO> listUserInterfaceInfoId(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);
}
