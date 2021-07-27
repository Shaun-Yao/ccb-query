package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honji.order.entity.Authority;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.CashDifference;
import com.honji.order.model.BillDTO;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DifferenceDTO;
import com.honji.order.service.IAuthorityService;
import com.honji.order.service.IBillService;
import com.honji.order.service.ICashBalanceService;
import com.honji.order.service.ICashDifferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2021-07-22
 */
@Controller
@RequestMapping("/cash-difference")
public class CashDifferenceController {


    @Autowired
    private ICashDifferenceService differenceService;

    @Autowired
    private IAuthorityService authorityService;

    @Autowired
    private ICashBalanceService cashBalanceService;

    @GetMapping("/index")
    public String index(@RequestParam String shopCode, Model model) {
        QueryWrapper<CashBalance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("khdm", shopCode);
        CashBalance cashBalance = cashBalanceService.getOne(queryWrapper);
        model.addAttribute("shopName", cashBalance.getKhmc());
        model.addAttribute("shopCode", shopCode);
        return "cash-difference";
    }

    @ResponseBody
    @GetMapping("/get")
    public CashDifference get(@RequestParam String id) {
        return differenceService.getById(id);
    }

    @ResponseBody
    @GetMapping("/list")
    public DataGridResult list(@RequestParam(defaultValue = "0") int offset, @RequestParam int limit,
                               @RequestParam String shopCode) {
        IPage<CashDifference> page = new Page<>(offset / limit + 1, limit);
        QueryWrapper<CashDifference> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_code", shopCode);
        queryWrapper.orderByDesc("date");
        return new DataGridResult(differenceService.page(page, queryWrapper));
    }

    @GetMapping("/to-query")
    public String query(@RequestParam String jobNum, Model model) {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_num", jobNum);
        List<Authority> authorities = authorityService.list(queryWrapper);
        model.addAttribute("authorities", authorities);
        return "difference-query";
    }

    @GetMapping("/query")
    @ResponseBody
    public DataGridResult query(DifferenceDTO differenceDTO) {

        return new DataGridResult(differenceService.query(differenceDTO));
    }

    @ResponseBody
    @PostMapping("/save")
    public boolean save(@ModelAttribute CashDifference cashDifference) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = cashDifference.getDate().format(dtf);
        //小票单号组成：门店的代码+“_xpd”+“小票日期”+后4位数字
        String number = cashDifference.getShopCode().concat("_xpd")
                .concat(date).concat(cashDifference.getNumber());
        cashDifference.setNumber(number);
        return differenceService.saveOrUpdate(cashDifference);
    }


    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(String id) {
        return differenceService.removeById(id);
    }
}
