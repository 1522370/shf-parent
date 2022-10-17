package com.atguigu.en;

/**
 * 包名:com.atguigu.en
 *
 * @author Leevi
 * 日期2022-10-05  14:04
 */
public enum DictCodeEnum {
    ROOT("ROOT"),HOUSETYPE("houseType"),FLOOR("floor"),BUILDSTRUCTURE("buildStructure"),
    DECORATION("decoration"),DIRECTION("direction"),HOUSEUSE("houseUse"),PROVINCE("province"),BEIJING("beijing");
    private String code;

    public String getCode() {
        return code;
    }

    DictCodeEnum(String code) {
        this.code = code;
    }
}
