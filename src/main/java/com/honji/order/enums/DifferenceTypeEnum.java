package com.honji.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

//@Getter
public enum DifferenceTypeEnum {

    ZERO("zero", "顾客抹零"),
    CASH_SUPPLEMENT("cash_supplement", "现金补单"),
    CASH_ADJUSTMENT("cash_adjustment", "现金调整")
    ;
    DifferenceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @EnumValue
    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

//    @JsonValue
    public String getDesc() {
        return desc;
    }

}
