package com.fanyu.project.model.vo;

import lombok.Data;

/**
 * 用户调用信息接口视图（脱敏）
 *
 */

@Data
public class UserInterfaceInfoVO {

    /**
     * id
     */
    private Long id;

    /**
     * 调用用户 id
     */
    private Long userId;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 接口 id
     */
    private Long interfaceInfoId;

    /**
     * 接口名称
     */
    private String interfaceInfoName;
    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

}