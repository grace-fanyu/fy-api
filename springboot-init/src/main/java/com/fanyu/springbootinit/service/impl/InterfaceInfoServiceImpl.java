package com.fanyu.springbootinit.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanyu.springbootinit.common.ErrorCode;
import com.fanyu.springbootinit.constant.CommonConstant;
import com.fanyu.springbootinit.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.fanyu.springbootinit.model.entity.InterfaceInfo;

import com.fanyu.springbootinit.model.vo.InterfaceInfoVO;
import com.fanyu.springbootinit.exception.BusinessException;
import com.fanyu.springbootinit.exception.ThrowUtils;

import com.fanyu.springbootinit.service.InterfaceInfoService;
import com.fanyu.springbootinit.mapper.InterfaceInfoMapper;
import com.fanyu.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author ASUS
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2023-05-22 21:38:42
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService{


	@Override
	public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
		if (interfaceInfo == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		Long id = interfaceInfo.getId();
	    String description = interfaceInfo.getDescription();
		String url = interfaceInfo.getUrl();
		String requestHeader = interfaceInfo.getRequestHeader();
		String responseHeader = interfaceInfo.getResponseHeader();
		Integer status = interfaceInfo.getStatus();
		String method = interfaceInfo.getMethod();
		Long userId = interfaceInfo.getUserId();
		Date createTime = interfaceInfo.getCreateTime();
		Date updateTime = interfaceInfo.getUpdateTime();
		Integer isDelete = interfaceInfo.getIsDelete();


		String name = interfaceInfo.getName();
		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (StringUtils.isNotBlank(name) && name.length() > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
		}


	}

//	/**
//	 * 获取查询条件
//	 *
//	 * @param interfaceInfoQueryRequest
//	 * @return
//	 */
//	@Override
//	public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
//
//
//
//		if (interfaceInfoQueryRequest == null) {
//			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
//		}
//		Long id = interfaceInfoQueryRequest.getId();
//		String name = interfaceInfoQueryRequest.getName();
//		String description = interfaceInfoQueryRequest.getDescription();
//		String url = interfaceInfoQueryRequest.getUrl();
//		String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
//		String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
//		Integer status = interfaceInfoQueryRequest.getStatus();
//		String method = interfaceInfoQueryRequest.getMethod();
//		Long userId = interfaceInfoQueryRequest.getUserId();
//		String sortField = interfaceInfoQueryRequest.getSortField();
//		String sortOrder = interfaceInfoQueryRequest.getSortOrder();
//		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
//		queryWrapper.eq(id != null, "id", id);
//		queryWrapper.eq(StringUtils.isNotBlank(name), "name", name);
//		queryWrapper.eq(StringUtils.isNotBlank(description), "description", description);
//		queryWrapper.eq(StringUtils.isNotBlank(url), "url", url);
//		queryWrapper.eq(StringUtils.isNotBlank(responseHeader), "responseHeader", responseHeader);
//		queryWrapper.eq(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
//		queryWrapper.eq(status != null, "status", status);
//		queryWrapper.eq(StringUtils.isNotBlank(method), "method", method);
//		queryWrapper.eq(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
//		queryWrapper.eq(userId != null, "userId", userId);
//		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
//				sortField);
//		return queryWrapper;
//	}
//
//
//
//	/**
//	 * 获取帖子封装
//	 *
//	 * @param post
//	 * @param request
//	 * @return
//	 */
//	@Override
//	public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo post, HttpServletRequest request) {
//		return null;
//	}
//
//	/**
//	 * 分页获取帖子封装
//	 *
//	 * @param postPage
//	 * @param request
//	 * @return
//	 */
//	@Override
//	public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> postPage, HttpServletRequest request) {
//		return null;
//	}


}




