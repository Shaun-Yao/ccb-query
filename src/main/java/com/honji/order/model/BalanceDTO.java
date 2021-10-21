package com.honji.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceDTO {


    /**
     * 门店代码
     */
    private List<String> shopCodes ;

    /**
     * 日期
     */
    private String date;

    private int offset;

    private int limit;


}
