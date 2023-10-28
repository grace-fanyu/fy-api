package com.fanyu.project.model.vo;

import lombok.Data;

@Data
public class OrderVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 调用用户 id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;
    /**
     * 接口名
     */
    private String interfaceName;

    /**
     * 购买的调用次数
     */
    private Integer totalNum;

    /**
     * 花费的星琼
     */
    private Long priceStar;

    /**
     * 花费的钻石
     */
    private Long priceDiamond;

    /**
     * 0-未支付，1-已支付
     */
    private Integer status;


}
