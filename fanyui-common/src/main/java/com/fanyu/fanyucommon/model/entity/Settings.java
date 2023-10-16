package com.fanyu.fanyucommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基本设置信息表
 * @TableName settings
 */
@TableName(value ="settings")
@Data
public class Settings implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 每日签到获取星琼数量
     */
    private Long dailyStar;

    /**
     * 星琼与钻石兑换比例：钻石=？星琼
     */
    private Long starDiamond;

    /**
     * 星琼默认图片
     */
    private String starImg;

    /**
     * 钻石默认图片
     */
    private String diamondImg;

    /**
     * 用户默认图片
     */
    private String userImg;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}