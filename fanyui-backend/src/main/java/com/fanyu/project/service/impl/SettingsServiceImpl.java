package com.fanyu.project.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fanyu.fanyucommon.model.entity.Settings;
import com.fanyu.project.mapper.SettingsMapper;
import com.fanyu.project.service.SettingsService;
import org.springframework.stereotype.Service;

/**
 * 基本设置信息
* @author 凡雨
*/
@Service
public class SettingsServiceImpl extends ServiceImpl<SettingsMapper, Settings> implements SettingsService {

    /**
     * 获取设置
     * @return Settings
     */
    @Override
    public Settings getSettings() {
        return this.getById(1);
    }
}




