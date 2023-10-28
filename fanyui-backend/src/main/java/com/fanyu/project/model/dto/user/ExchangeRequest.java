package com.fanyu.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class ExchangeRequest implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 星琼
     */
    private Long userStar;
    /**
     * 钻石
     */
    private Long userDiamond;
    private static final long serialVersionUID = 1L;
}
