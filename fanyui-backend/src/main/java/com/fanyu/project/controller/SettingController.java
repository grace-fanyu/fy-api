package com.fanyu.project.controller;


import com.fanyu.fanyucommon.model.entity.Settings;
import com.fanyu.fanyucommon.model.entity.Settings;
import com.fanyu.project.annotation.AuthCheck;
import com.fanyu.project.common.BaseResponse;
import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.common.ResultUtils;
import com.fanyu.project.constant.UserConstant;
import com.fanyu.project.exception.BusinessException;
import com.fanyu.project.exception.ThrowUtils;
import com.fanyu.project.model.dto.settings.SettingUpdateRequest;
import com.fanyu.project.service.SettingsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 设置接口
 *
 */
@RestController
@RequestMapping("/setting")
@Slf4j
public class SettingController {

    @Resource
    private SettingsService settingsService;

    private static final Long ID=1L;


    /**
     * 更改设置
     *
     * @param settingUpdateRequest SettingsUpdateRequest
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSetting(@RequestBody SettingUpdateRequest settingUpdateRequest,
                                            HttpServletRequest request) {
        if (settingUpdateRequest == null ) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Settings setting = new Settings();
        setting.setId(ID);
        BeanUtils.copyProperties(settingUpdateRequest, setting);
        boolean result = settingsService.updateById(setting);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 获取设置
     *
     * @param request HttpServletRequest
     * @return BaseResponse
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Settings> getSetting(HttpServletRequest request) {
        Settings settings = settingsService.getSettings();
        return ResultUtils.success(settings);
    }






}
