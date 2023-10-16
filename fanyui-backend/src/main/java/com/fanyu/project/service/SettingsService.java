package com.fanyu.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fanyu.fanyucommon.model.entity.Settings;

/**
 * 基本设置信息
* @author 凡雨
*/
public interface SettingsService extends IService<Settings> {

    /**
     * 获取设置
     * @return Settings
     */
    Settings getSettings();


}
