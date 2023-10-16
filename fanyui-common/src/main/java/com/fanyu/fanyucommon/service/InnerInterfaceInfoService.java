package com.fanyu.fanyucommon.service;


import com.fanyu.fanyucommon.model.entity.InterfaceInfo;

/**
 * 内部接口信息服务
 * @author 凡雨
*/
public interface InnerInterfaceInfoService {

	/**
	 * 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数）
	 */
	InterfaceInfo getInterfaceInfo(String path, String method);


}