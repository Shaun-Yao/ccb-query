package com.honji.order.controller;


import com.honji.order.service.ICashBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
@Controller
@RequestMapping("/cash-balance")
public class CashBalanceController {

    @Autowired
    private ICashBalanceService cashBalanceService;

    @Autowired
    private HttpSession session;

    @GetMapping("/calculate")
    @ResponseBody
    public double calculate() {
        String user = (String) session.getAttribute("user");
        return cashBalanceService.calBalance(user);
    }

}
