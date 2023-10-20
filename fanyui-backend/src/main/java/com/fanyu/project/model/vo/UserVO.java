package com.fanyu.project.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 */

@Data
public class UserVO  {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 星琼
     */
    private Long userStar;

    /**
     * 钻石
     */
    private Long userDiamond;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}