package com.yang.common.constant;

/**
 * 属性类型常量
 * @Author: shanfy
 * @Date: 2022/4/5 15:25
 */
public enum AttrEnum {
    ATTR_TYPE_BASE(0,"基本属性"),
    ATTR_TYPE_SALE(1,"销售属性");
    private int code;
    private String message;
    AttrEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return this.code;
    }

    public String getMessage(){
        return this.message;
    }
}
