package com.pjb.springbootjwt.zhddkk.enumx;

public enum OperationEnum {
    INSERT("增加"),

    DELETE("删除"),
    
    EDIT("编辑"),
    
    UPDATE("更新"),
    
    QUERY("查询"),
    
    PAGE("页面"),

    BATCHDELETE("批量删除"),

    EXPORT("导出");

    String value;

    OperationEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
