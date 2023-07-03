package com.fanyu.springbootinit.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fanyu.springbootinit.model.entity.InterfaceInfo;

/**
* @author ASUS
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2023-05-22 21:38:42
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

	/**
	 * 校验
	 *
	 * @param post
	 * @param add
	 */
	void validInterfaceInfo(InterfaceInfo post, boolean add);


}