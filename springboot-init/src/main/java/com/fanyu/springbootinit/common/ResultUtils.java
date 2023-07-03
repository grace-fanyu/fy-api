package com.fanyu.springbootinit.common;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * 返回工具类
 *
 * @author fanyu
 */
public class ResultUtils {

    /**
     * 成功
     *
     * @param data 数据
     * @param <T> T
     * @return BaseResponse
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode 响应码
     * @return BaseResponse
     */
    @NotNull
    @Contract("_ -> new")
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param code 响应码
     * @param message message
     * @return BaseResponse
     */
    public static BaseResponse error(int code, String message) {
        return new BaseResponse(code, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode 响应码
     * @return BaseResponse
     */
    public static BaseResponse error(ErrorCode errorCode, String message) {
        return new BaseResponse(errorCode.getCode(), null, message);
    }
}
