package com.honji.order.controller;


import com.honji.order.service.IFundBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2022-08-26
 */
@Controller
@RequestMapping("/fund-bill")
public class FundBillController {


    @Autowired
    private IFundBillService fundBillService;


}
