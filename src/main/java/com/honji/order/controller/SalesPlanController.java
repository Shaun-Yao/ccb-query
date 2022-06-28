package com.honji.order.controller;


import com.honji.order.entity.Sale;
import com.honji.order.entity.SalesPlan;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.ISaleService;
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

    @Autowired
    private ISaleService saleService;


    @GetMapping("/index")
    public String index(@RequestParam String jobNum, Model model) {

        model.addAttribute("jobNum", jobNum);
        return "sales-plan-follow-up";
    }

    @GetMapping("/get")
    @ResponseBody
    public Sale get(@RequestParam String id) {

        return saleService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam String shopCode, @RequestParam int offset, @RequestParam int limit) {
        return new DataGridResult(saleService.listForIndex(shopCode, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute SalesPlan salesPlan) {
        System.out.println(salesPlan.getDetails().get(0).getConvention());
        return true;
//        return saleService.saveOrUpdate(sale);
    }


    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam String id) {
        return saleService.removeById(id);
    }


    @GetMapping("/getPerformance")
    @ResponseBody
    public Map<String, Object> getPerformance(@RequestParam String date, @RequestParam String shopCode) {

        return salesPlanService.getPerformance(date, shopCode);
    }


}
