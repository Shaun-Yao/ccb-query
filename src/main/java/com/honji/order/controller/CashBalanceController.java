package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.Bank;
import com.honji.order.entity.CashBalance;
import com.honji.order.service.ICashBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

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



    @GetMapping("/calculate")
    @ResponseBody
    public double calculate(@RequestParam String shopCode) {
        return cashBalanceService.calBalance(shopCode);
    }

    @GetMapping("/index")
    public String index() {

        return "cash-balance";
    }

}
