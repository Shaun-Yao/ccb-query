package com.honji.order.controller;


import com.honji.order.entity.CashBalance;
import com.honji.order.entity.SalesPlan;
import com.honji.order.entity.SalesPlanDetails;
import com.honji.order.mapper.SalesPlanMapper;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.ICashBalanceService;
import com.honji.order.service.ISalesPlanDetailsService;
import com.honji.order.service.ISalesPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2020-08-30
 */
@Slf4j
@Controller
@RequestMapping("/sales-plan-details")
public class SalesPlanDetailsController {



    @Autowired
    private ISalesPlanDetailsService salesPlanDetailsService;
    @Autowired
    private ISalesPlanService salesPlanService;
    @Autowired
    private ICashBalanceService cashBalanceService;
    @Autowired
    private SalesPlanMapper salesPlanMapper;


    @GetMapping("/index")
    public String index(@RequestParam(required = false) String jobNum,
                        @RequestParam(required = false) String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {//有id为修改
            SalesPlan salesPlan = salesPlanService.getById(id);
            model.addAttribute("salesPlan", salesPlan);
            model.addAttribute("planId", id);
        } else {//没id为新建
            Map<String, Object> map = salesPlanMapper.selectName(jobNum);
            model.addAttribute("name", map.get("name"));
            model.addAttribute("createDate", LocalDate.now());
        }
        List<CashBalance> shops = cashBalanceService.list();
        model.addAttribute("shops", shops);
        model.addAttribute("jobNum", jobNum);
        return "sales-plan-details";
    }

    @GetMapping("/get")
    @ResponseBody
    public SalesPlanDetails get(@RequestParam String id) {

        return salesPlanDetailsService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam String planId, @RequestParam int offset, @RequestParam int limit) {
        return new DataGridResult(salesPlanDetailsService.listForIndex(planId, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public String save(@ModelAttribute SalesPlanDetails salesPlanDetails) {
        salesPlanDetailsService.saveOrUpdate(salesPlanDetails);
        return salesPlanDetails.getId();
    }


    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam String id) {
        return salesPlanDetailsService.removeById(id);
    }



}
