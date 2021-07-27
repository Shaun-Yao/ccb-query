package com.honji.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DifferenceDTO {



    /**
     * 门店代码
     */
    private List<String> shopCodes ;

    /**
     * 小票开始日期
     */
    private String begin;

    /**
     * 小票结束日期
     */
    private String end;


    private int offset;

    private int limit;


}
