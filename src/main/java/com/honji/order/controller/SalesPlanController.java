package com.honji.order.controller;


import com.honji.order.entity.SalesPlan;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.ISalesPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/sales-plan")
public class SalesPlanController {



    @Autowired
    private ISalesPlanService salesPlanService;


    @GetMapping("/index")
    public String index(@RequestParam String jobNum, Model model) {

        model.addAttribute("jobNum", jobNum);
        return "sales-plan";
    }

    @GetMapping("/get")
    @ResponseBody
    public SalesPlan get(@RequestParam String id) {

        return salesPlanService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam String jobNum, @RequestParam int offset, @RequestParam int limit) {
        return new DataGridResult(salesPlanService.listForIndex(jobNum, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public String save(@ModelAttribute SalesPlan salesPlan) {
        salesPlanService.saveOrUpdate(salesPlan);
        return salesPlan.getId();//返回id方便子表新增，直接返回值非json格式
    }


    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam String id) {
        return salesPlanService.removeById(id);
    }


    @GetMapping("/getPerformance")
    @ResponseBody
    public Map<String, Object> getPerformance(@RequestParam String date, @RequestParam String shopCode) {

        return salesPlanService.getPerformance(date, shopCode);
    }


}
