package com.atguigu.en;

/**
 * 包名:com.atguigu.en
 *
 * @author Leevi
 * 日期2022-10-07  14:32
 */
public enum YesOrNo {
    YES(1,"可用"),NO(0,"不可用");
    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    YesOrNo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
