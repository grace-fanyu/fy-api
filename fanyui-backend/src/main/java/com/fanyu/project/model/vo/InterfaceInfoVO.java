package com.fanyu.project.model.vo;

import lombok.Data;

/**
 * 接口视图(脱敏)
 *
 */



@Data
public class InterfaceInfoVO  {
    /**
     * 接口id
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;
    /**
     * 请求类型
     */
    private String method;

    /**
     * 星琼/100次
     */
    private Long priceStar;

    /**
     * 钻石/100次
     */
    private Long priceDiamond;

    /**
     * 调用次数
     */
    private Integer totalNum;

    private static final long serialVersionUID = 1L;
}