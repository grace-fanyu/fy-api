package com.fanyu.project.model.vo;

import lombok.Data;

@Data
public class InterfaceInfoListVO {

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
    private Long totalNum;
}
