package com.honji.order.controller;


import com.honji.order.entity.Sale;
import com.honji.order.entity.SalesPlan;
import com.honji.order.mapper.SalesPlanMapper;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.ISaleService;
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
@RequestMapping("/sales-plan-details")
public class SalesPlanDetailsController {



    @Autowired
    private ISaleService saleService;
    @Autowired
    private SalesPlanMapper salesPlanMapper;


    @GetMapping("/index")
    public String index(@RequestParam String jobNum, Model model) {

        Map<String, Object> map = salesPlanMapper.selectName(jobNum);
//        System.out.println("name==" + map.get("name"));
//        System.out.println("bb==" + map.get("bb"));
        model.addAttribute("jobNum", jobNum);
        model.addAttribute("name", map.get("name"));
        return "sales-plan-details";
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



}
