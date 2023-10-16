package com.fanyu.project.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fanyu.fanyucommon.model.entity.InterfaceInfo;
import com.fanyu.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.fanyu.project.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 针对表【interface_info(接口信息)】的数据库操作Service
 * @author 凡雨
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

	/**
	 * 校验
	 *
	 * @param post InterfaceInfo
	 * @param add boolean
	 */
	void validInterfaceInfo(InterfaceInfo post, boolean add);

	/**
	 * 分页查询
	 * @param interfaceInfoQueryRequest InterfaceInfoQueryRequest
	 * @param request HttpServletRequest
	 * @return Page<InterfaceInfo>
	 */
	Page<InterfaceInfo> interfacePage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request);

	/**分页查询
	 *
	 * @param interfaceInfoQueryRequest InterfaceInfoQueryRequest
	 * @param request HttpServletRequest
	 * @return  Page<InterfaceInfoVO>
	 */
	List<InterfaceInfoVO> interfaceVOPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request);

}