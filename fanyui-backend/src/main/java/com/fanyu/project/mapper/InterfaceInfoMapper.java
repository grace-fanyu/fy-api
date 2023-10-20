package com.fanyu.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fanyu.fanyucommon.model.entity.InterfaceInfo;
import com.fanyu.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.fanyu.project.model.vo.InterfaceInfoListVO;

import java.util.List;

/**
* 接口信息数据库操作
*/
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    List<InterfaceInfoListVO> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

}




