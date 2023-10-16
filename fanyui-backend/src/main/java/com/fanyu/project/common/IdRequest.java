package com.fanyu.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * id请求
 *
 * @author fanyu
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;

}