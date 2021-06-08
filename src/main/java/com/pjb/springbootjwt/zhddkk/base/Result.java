package com.pjb.springbootjwt.zhddkk.base;

/**
 * 响应结构.
 * @param <T> 返回数据类型
 */
public class Result<T> {

    public static final Integer CODE_SUCCESS = 1;

    public static final Integer CODE_FAIL = 0;

    public static final String MSG_SUCCESS = "操作成功";

    public static final String MSG_FAIL = "操作失败";

    private Integer code;

    private String msg;

    private T data;

    public static <T> Result<T> build(Integer status, String msg) {
        return new Result<T>(status, msg, null);
    }

    public static <T> Result<T> build(Integer status, String msg, T data) {
        return new Result<T>(status, msg, data);
    }

    public static <T> Result<T> ok() {
        return new Result<T>(CODE_SUCCESS, MSG_SUCCESS, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> fail() {
        return new Result<T>(CODE_FAIL, MSG_FAIL, (T)MSG_FAIL);
    }

    public static <T> Result<T> fail(T data) {
        return new Result<T>(CODE_FAIL, MSG_FAIL, data);
    }

    public Result() {
    }

    public static <T> Result<T> getResult(T t) {
        return new Result<>(t);
    }

    /**
     * 构造函数.
     * @param status 返回码
     * @param msg 消息
     * @param data 返回内容
     */
    public Result(Integer status, String msg, T data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 构造函数.
     * @param data 返回的数据
     */
    public Result(T data) {
        this.code = 1;
        this.msg = MSG_SUCCESS;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
