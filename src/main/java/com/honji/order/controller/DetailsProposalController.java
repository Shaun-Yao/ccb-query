package com.honji.order.controller;


import com.honji.order.entity.DetailsProposal;
import com.honji.order.entity.SalesPlan;
import com.honji.order.entity.SalesPlanDetails;
import com.honji.order.mapper.SalesPlanMapper;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.IDetailsProposalService;
import com.honji.order.service.ISalesPlanDetailsService;
import com.honji.order.service.ISalesPlanService;
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
    @Autowired
    private ISalesPlanService salesPlanService;
    @Autowired
    private SalesPlanMapper salesPlanMapper;


    @GetMapping("/index")
    public String index(@RequestParam(required = false) String id, @RequestParam(required = false) String planId,
                        @RequestParam(required = false) String jobNum, Model model) {
        boolean enableFeedback = false;//是否开启编辑反馈意见
        boolean isCreator = false;//是否表单创建人
        if(StringUtils.isNotEmpty(id)) {
            SalesPlanDetails details = salesPlanDetailsService.getById(id);
            String pid = details.getPlanId();
            SalesPlan salesPlan = salesPlanService.getById(pid);
            int result = salesPlanMapper.belongTo(pid, jobNum);
            if (result > 0) {//如果有记录则查看人与此salesplan所属大区经理相同，可以编辑反馈意见
                enableFeedback = true;
            }

            if (salesPlan.getJobNum().equals(jobNum)) {
                isCreator = true;
            }
            model.addAttribute("details", details);
        }

        model.addAttribute("enableFeedback", enableFeedback);
        model.addAttribute("isCreator", isCreator);
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
