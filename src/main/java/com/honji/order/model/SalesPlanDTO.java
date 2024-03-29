package com.honji.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesPlanDTO {


    /**
     * 工号
     */
    private String jobNum;

    /**
     * 门店代码
     * bootstrpa-table 传数组参数没有加上数组index，加上traditional:true 也无效
     */
    private String area;
    private List<String> shopCodes ;
    private int feedbackState;
    private String performDate;
    boolean isAdmin = false;
    boolean isManager = false;

    private int offset;
    private int limit;


}
