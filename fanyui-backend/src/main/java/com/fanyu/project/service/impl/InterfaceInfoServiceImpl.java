package com.fanyu.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanyu.fanyucommon.model.entity.InterfaceInfo;
import com.fanyu.project.common.ErrorCode;

import com.fanyu.project.constant.CommonConstant;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.exception.ThrowUtils;

import com.fanyu.project.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.fanyu.project.model.vo.InterfaceInfoVO;
import com.fanyu.project.service.InterfaceInfoService;
import com.fanyu.project.mapper.InterfaceInfoMapper;
import com.fanyu.project.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 接口信息业务实现
* @author 凡雨
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService{


	@Override
	public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
		if (interfaceInfo == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}

		String name = interfaceInfo.getName();
		String url = interfaceInfo.getUrl();
		String method = interfaceInfo.getMethod();

		// 创建时，参数不能为空
		if (add) {
			ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
			ThrowUtils.throwIf(StringUtils.isAnyBlank(url),ErrorCode.PARAMS_ERROR);
			ThrowUtils.throwIf(StringUtils.isAnyBlank(method),ErrorCode.PARAMS_ERROR);
		}
		// 有参数则校验
		if (StringUtils.isNotBlank(name) && name.length() > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
		}


	}

	/**
	 * 分页查询
	 *
	 * @param interfaceInfoQueryRequest InterfaceInfoQueryRequest
	 * @param request                   HttpServletRequest
	 * @return Page<InterfaceInfo>
	 */
	@Override
	public Page<InterfaceInfo> interfacePage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {
		InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
		BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
		long current = interfaceInfoQueryRequest.getCurrent();
		long size = interfaceInfoQueryRequest.getPageSize();
		String sortField = interfaceInfoQueryRequest.getSortField();
		String sortOrder = interfaceInfoQueryRequest.getSortOrder();
		String description = interfaceInfoQuery.getDescription();
		// description 需支持模糊搜索
		interfaceInfoQuery.setDescription(null);
		// 限制爬虫
		if (size > 50) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
		queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
		queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
				sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
		return this.page(new Page<>(current, size), queryWrapper);
	}

	/**
	 * 分页查询
	 *
	 * @param interfaceInfoQueryRequest InterfaceInfoQueryRequest
	 * @param request                   HttpServletRequest
	 * @return Page<InterfaceInfoVO>
	 */
	@Override
	public List<InterfaceInfoVO> interfaceVOPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest, HttpServletRequest request) {


		List<InterfaceInfoVO> interfaceInfoVOS = this.baseMapper.listTopInvokeInterfaceInfo(interfaceInfoQueryRequest);
		return interfaceInfoVOS;
	}


//	/**
//	 * 获取查询条件
//	 *
//	 * @param interfaceInfoQueryRequest
//	 * @return
//	 */
//	@Override
	public QueryWrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {



		if (interfaceInfoQueryRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
		}
		Long id = interfaceInfoQueryRequest.getId();
		String name = interfaceInfoQueryRequest.getName();
		String description = interfaceInfoQueryRequest.getDescription();
		String url = interfaceInfoQueryRequest.getUrl();
		String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
		String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
		Integer status = interfaceInfoQueryRequest.getStatus();
		String method = interfaceInfoQueryRequest.getMethod();
		Long userId = interfaceInfoQueryRequest.getUserId();
		String sortField = interfaceInfoQueryRequest.getSortField();
		String sortOrder = interfaceInfoQueryRequest.getSortOrder();
		QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(id != null, "id", id);
		queryWrapper.eq(StringUtils.isNotBlank(name), "name", name);
		queryWrapper.eq(StringUtils.isNotBlank(description), "description", description);
		queryWrapper.eq(StringUtils.isNotBlank(url), "url", url);
		queryWrapper.eq(StringUtils.isNotBlank(responseHeader), "responseHeader", responseHeader);
		queryWrapper.eq(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
		queryWrapper.eq(status != null, "status", status);
		queryWrapper.eq(StringUtils.isNotBlank(method), "method", method);
		queryWrapper.eq(StringUtils.isNotBlank(requestHeader), "requestHeader", requestHeader);
		queryWrapper.eq(userId != null, "userId", userId);
		queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
				sortField);
		return queryWrapper;
	}
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




