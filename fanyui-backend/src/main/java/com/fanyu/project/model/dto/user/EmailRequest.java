package com.fanyu.project.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮箱请求
 */
@Data
public class EmailRequest implements Serializable {
    private String email;
}
