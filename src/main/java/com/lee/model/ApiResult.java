package com.lee.model;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author: lsw
 * @date: 2023/10/18 13:32
 */
public class ApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int code;
    private T data;
    private String msg;


    private ApiResult(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }


    public static <T> ApiResult<T> data(T data) {
        return data(data, "操作成功");
    }

    public static <T> ApiResult<T> data(T data, String msg) {
        return data(200, data, msg);
    }

    public static <T> ApiResult<T> data(int code, T data, String msg) {
        return new ApiResult(code, data, data == null ? "" : msg);
    }

    public static <T> ApiResult<T> fail(String msg){
        return data(400, null, msg);
    }

    public static <T> ApiResult<T> success(String msg){
        return data(200, null, msg);
    }



    public int getCode() {
        return this.code;
    }
    public T getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(final int code) {
        this.code = code;
    }

    public void setData(final T data) {
        this.data = data;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }


    public ApiResult() {
    }

}
