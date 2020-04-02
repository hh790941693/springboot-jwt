package com.pjb.springbootjwt.common.vo;


/**
 * 响应结构
 * @param <T>
 */
public class Result<T> {

    public final static Integer CODE_SUCCESS = 1;

    public final static Integer CODE_FAIL = 0;

    public final static String MSG_SUCCESS = "操作成功";

    public final static String MSG_FAIL = "操作失败";

    private Integer code;

    private String msg;

    private T data;

    public static <T> Result<T> build(Integer status, String msg, T data) {
        return new Result<T>(status, msg, data);
    }

    public static <T> Result<T> ok() {
        return new Result<T>(CODE_SUCCESS, MSG_SUCCESS, (T)new Object());
    }

    public static <T> Result<T> ok(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> fail() {
        return new Result<T>(CODE_FAIL, MSG_FAIL, (T)new Object());
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<T>(CODE_FAIL, msg, (T)new Object());
    }

    public Result() {

    }

    public static <T> Result<T> build(Integer status, String msg) {
        return new Result<T>(status, msg, (T)new Object());
    }

    public static <T> Result<T> getResult(T t) {
        Result<T> result = new Result<>(t);
        return result;
    }

    public Result(Integer status, String msg, T data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

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

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
