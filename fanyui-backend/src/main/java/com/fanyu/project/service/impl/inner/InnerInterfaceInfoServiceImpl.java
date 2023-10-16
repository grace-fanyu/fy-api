package com.fanyu.project.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fanyu.fanyucommon.model.entity.InterfaceInfo;
import com.fanyu.fanyucommon.service.InnerInterfaceInfoService;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
* @author 凡雨
*/
@DubboService(version = "1.0.1")
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

	@Resource
	InterfaceInfoMapper interfaceInfoMapper;

	/**
	 * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
	 *
	 * @param url 请求路径
	 * @param method 请求方法
	 */
	@Override
	public InterfaceInfo getInterfaceInfo(String url, String method) {
		if (StringUtils.isAnyBlank(url,method)){
			throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
		}
		QueryWrapper<InterfaceInfo> interfaceInfoQueryWrapper = new QueryWrapper<>();
		interfaceInfoQueryWrapper.eq("url",url);
		interfaceInfoQueryWrapper.eq("method",method);

		return interfaceInfoMapper.selectOne(interfaceInfoQueryWrapper);
	}
}




