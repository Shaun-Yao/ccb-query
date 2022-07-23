package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.honji.order.entity.Area;
import com.honji.order.entity.DetailsProposal;
import com.honji.order.entity.SalesPlan;
import com.honji.order.entity.SalesPlanDetails;
import com.honji.order.mapper.SalesPlanMapper;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.SalesPlanVO;
import com.honji.order.service.IAreaService;
import com.honji.order.service.IDetailsProposalService;
import com.honji.order.service.ISalesPlanDetailsService;
import com.honji.order.service.ISalesPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    private IAreaService areaService;

    @Autowired
    private IDetailsProposalService detailsProposalService;
    @Autowired
    private SalesPlanMapper salesPlanMapper;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/index")
    public String index(@RequestParam(required = false) String jobNum,
                        @RequestParam(required = false) String id, Model model) {
        boolean enableFeedback = false;//是否开启编辑反馈意见
        if(StringUtils.isNotEmpty(id)) {
            int result = salesPlanMapper.belongTo(id, jobNum);
            if (result > 0) {//如果有记录则查看人与此salesplan所属大区经理相同，可以编辑反馈意见
                enableFeedback = true;
            }
        }
        model.addAttribute("enableFeedback", enableFeedback);

        if (StringUtils.isNotEmpty(id)) {//有id为修改
            SalesPlan salesPlan = salesPlanService.getById(id);
            model.addAttribute("salesPlan", salesPlan);
            model.addAttribute("planId", id);
        } else {//没id为新建
            Map<String, Object> map = salesPlanMapper.selectName(jobNum);
            model.addAttribute("name", map.get("name"));
            model.addAttribute("createDate", LocalDate.now());
        }
        List<Area> areas = areaService.list();
        model.addAttribute("areas", areas);
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
    public DataGridResult list(@RequestParam String planId,
                               @RequestParam int offset, @RequestParam int limit) {
        return new DataGridResult(salesPlanDetailsService.listForIndex(planId, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public String save(@ModelAttribute SalesPlanDetails salesPlanDetails, @RequestParam boolean wantToNotify) {
        if (wantToNotify) {
            SalesPlan salesPlan = salesPlanMapper.selectById(salesPlanDetails.getPlanId());
            String jobNum = salesPlanMapper.selectManagerByShop(salesPlan.getShopCode());
            salesPlanMapper.notify(jobNum, "您有业绩下降原因上报，请查看！", "");
        }
        salesPlanDetailsService.saveOrUpdate(salesPlanDetails);
        return salesPlanDetails.getId();
    }

    @PostMapping("/saveFeedback")
    @ResponseBody
    public void saveFeedback(@RequestParam String id, @RequestParam String feedback) {

        salesPlanDetailsService.saveFeedback(id, feedback);
    }


    @PostMapping("/notify")
    @ResponseBody
    public int notify(@RequestParam String planId) {
        SalesPlanVO salesPlan = salesPlanMapper.selectOne(planId);
        UpdateWrapper<SalesPlan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", planId);
        updateWrapper.set("state", 1);
        salesPlanService.update(updateWrapper);//将状态更新为已通知

        String jobNum = salesPlanMapper.selectManagerByShop(salesPlan.getShopCode());
        String link = request.getScheme() +"://" + request.getServerName()
                + ":" +request.getServerPort()
                + "/sales-plan-details/index?jobNum="
                .concat(jobNum).concat("&id=").concat(salesPlan.getId());
//        String link = "http://localhost:9006/sales-plan-details/index?jobNum="
//                .concat(jobNum).concat("&id=").concat(salesPlan.getId());
        String message = salesPlan.getShopCode().concat(salesPlan.getShopName())
                .concat("(").concat(salesPlan.getPerformDate()).concat(")业绩下降原因上报，请查看！");
        return salesPlanMapper.notify(jobNum, message, link);
    }

    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam String id) {
        SalesPlanDetails salesPlanDetails =  salesPlanDetailsService.getById(id);
        QueryWrapper<DetailsProposal> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("details_id", id);
        List<DetailsProposal> proposals = detailsProposalService.list(queryWrapper);
        //已经有方案或者有反馈不允许删除
        if (proposals.size() > 0 || salesPlanDetails.getFeedback().length() > 0) {
            return false;
        }
        return salesPlanDetailsService.removeById(id);
    }



}
