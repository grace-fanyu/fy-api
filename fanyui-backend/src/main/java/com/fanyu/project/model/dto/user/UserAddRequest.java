package com.fanyu.project.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户创建请求
 *
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户角色: user, admin
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

    private static final long serialVersionUID = 1L;
}