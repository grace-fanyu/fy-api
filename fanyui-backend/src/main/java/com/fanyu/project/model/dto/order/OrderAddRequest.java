package com.fanyu.project.model.dto.order;

import lombok.Data;

import java.io.Serializable;


@Data
public class OrderAddRequest implements Serializable {

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

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

    private static final long serialVersionUID = 1L;
}
