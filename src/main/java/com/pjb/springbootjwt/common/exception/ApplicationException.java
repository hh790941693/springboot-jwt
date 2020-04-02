package com.pjb.springbootjwt.common.exception;

public class ApplicationException extends RuntimeException{
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ApplicationException(){
        super();
    }

    public ApplicationException(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
