package com.honji.order.controller;


import com.honji.order.entity.DetailsProposal;
import com.honji.order.entity.SalesPlanDetails;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.IDetailsProposalService;
import com.honji.order.service.ISalesPlanDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/details-proposal")
public class DetailsProposalController {



    @Autowired
    private IDetailsProposalService detailsProposalService;
    @Autowired
    private ISalesPlanDetailsService salesPlanDetailsService;


    @GetMapping("/index")
    public String index(@RequestParam(required = false) String id, @RequestParam(required = false) String planId,
                        @RequestParam(required = false) String jobNum, Model model) {
        if(StringUtils.isNotEmpty(id)) {
            SalesPlanDetails details = salesPlanDetailsService.getById(id);
            model.addAttribute("details", details);
        }

        model.addAttribute("planId", planId);
        model.addAttribute("jobNum", jobNum);
        return "sales-plan-details-proposal";
    }

    @GetMapping("/get")
    @ResponseBody
    public DetailsProposal get(@RequestParam String id) {

        return detailsProposalService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam String detailsId, @RequestParam int offset, @RequestParam int limit) {
        return new DataGridResult(detailsProposalService.listForIndex(detailsId, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute DetailsProposal detailsProposal) {
        return detailsProposalService.saveOrUpdate(detailsProposal);
    }


    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam String id) {
        return detailsProposalService.removeById(id);
    }



}
