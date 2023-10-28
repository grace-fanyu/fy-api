package com.fanyu.project.model.dto.order;

import lombok.Data;

import java.io.Serializable;
@Data
public class OrderUpdateRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 购买的调用次数
     */
    private Integer totalNum;

    /**
     * 0-未支付，1-已支付
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}
