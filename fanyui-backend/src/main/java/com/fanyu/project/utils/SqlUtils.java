package com.fanyu.project.utils;

import com.fanyu.project.common.ErrorCode;
import com.fanyu.project.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * SQL 工具
 *
 */
public class SqlUtils {

    /**
     * 校验排序字段是否合法（防止 SQL 注入）
     *
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }

    public static String setWildcard(String str){
        if (StringUtils.isAnyBlank(str)) {
            return null;
        }
        return "%"+str+"%";
    }
}
