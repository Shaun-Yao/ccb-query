package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.Bank;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DepositDTO;
import com.honji.order.service.ICashBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
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

    @GetMapping("/get")
    @ResponseBody
    public CashBalance get(String id) {
        CashBalance cashBalance = cashBalanceService.getById(id);
        return cashBalance;
    }

    @GetMapping("/query")
    @ResponseBody
    public DataGridResult query(String shopCode, int offset, int limit) {
        return new DataGridResult(cashBalanceService.listByShopCode(shopCode, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute CashBalance cashBalance) {
        return cashBalanceService.saveOrUpdate(cashBalance);
    }

    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam List<String> ids) {
        return cashBalanceService.removeByIds(ids);
    }
}
