package com.fanyu.project.model.dto.order;

import com.fanyu.project.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class OrderQueryRequest extends PageRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;


}
