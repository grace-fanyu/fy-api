package com.fanyu.project.model.dto.settings;

import lombok.Data;

import java.io.Serializable;

@Data
public class SettingUpdateRequest implements Serializable {


    /**
     * 每日签到获取的星琼数量设置
     */
    private Integer dailyStar;
    /**
     * 钻石换星琼的比例设置
     */
    private Integer starDiamond;
    /**
     * 用户默认头像地址设置
     */
    private String userImg;

    private static final long serialVersionUID = 1L;
}
