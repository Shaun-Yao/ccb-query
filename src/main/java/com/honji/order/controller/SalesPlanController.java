package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.*;
import com.honji.order.mapper.AreaMapper;
import com.honji.order.mapper.SalesPlanMapper;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.SalesPlanDTO;
import com.honji.order.model.SalesPlanVO;
import com.honji.order.service.IAreaService;
import com.honji.order.service.IDetailsProposalService;
import com.honji.order.service.ISalesPlanDetailsService;
import com.honji.order.service.ISalesPlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
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
@RequestMapping("/sales-plan")
public class SalesPlanController {



    @Autowired
    private ISalesPlanService salesPlanService;

    @Autowired
    private ISalesPlanDetailsService salesPlanDetailsService;

    @Autowired
    private IDetailsProposalService detailsProposalService;
    @Autowired
    private IAreaService areaService;

    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private SalesPlanMapper salesPlanMapper;


    @GetMapping("/index")
    public String index(@RequestParam String jobNum, Model model) {
        String manager = salesPlanMapper.selectAManager(jobNum);
        if (StringUtils.isNotEmpty(manager)) {//
            model.addAttribute("isManager", true);
        }
        List<Area> areas = areaService.list();
        model.addAttribute("areas", areas);
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
    public DataGridResult list(SalesPlanDTO salesPlanDTO) {
        return new DataGridResult(salesPlanService.listForIndex(salesPlanDTO));
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
        QueryWrapper<SalesPlanDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", id);
        List<SalesPlanDetails> detailsList = salesPlanDetailsService.list(queryWrapper);
        if (detailsList.size() > 0) {//已经有原因不允许删除
            return false;
        }
        return salesPlanService.removeById(id);
    }


    @GetMapping("/getPerformance")
    @ResponseBody
    public Map<String, Object> getPerformance(@RequestParam String date, @RequestParam String shopCode) {

        return salesPlanService.getPerformance(date, shopCode);
    }

    @GetMapping("/listShops")
    @ResponseBody
    public List<Shop> listShops(@RequestParam String area) {

        return areaMapper.listShops(area);
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(SalesPlanDTO salesPlanDTO, HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();


        String columnNames[] = { "制表人", "制表日期", "销售区域", "店铺编码", "店铺名称", "业绩分析时点",
                "当月业绩", "同期业绩"};// 列名
        String detailsColumnNames[] = { "原因类型", "主要原因", "原因分析", "方案类型", "方案", "大区经理反馈",
                "方案确定", "开始日期", "结束日期", "频率", "执行人", "结果"};// 原因列名
//        String proposalColumnNames[] = { "方案", "日期", "原因分析", "常规方案", "创新方案", "大区经理反馈",
//                "执行人", "结果"};// 方案列名
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));


        List<SalesPlanVO> plans = salesPlanService.listForExport(salesPlanDTO);
        for (int i = 0; i < plans.size(); i++) {
            SalesPlanVO plan = plans.get(i);
            String shopName = plan.getShopName();
            String performDate = plan.getPerformDate();
            XSSFSheet sheet = workbook.createSheet(shopName.concat(performDate));
            Row headRow = sheet.createRow(0);
            for (int j = 0; j < columnNames.length; j++) {
                headRow.createCell(j).setCellValue(columnNames[j]);
            }

            Row row = sheet.createRow( 1);
            row.createCell(0).setCellValue(plan.getName());
            Cell dateCell = row.createCell(1);
            dateCell.setCellValue(plan.getCreateDate());
            dateCell.setCellStyle(cellStyle);

            row.createCell(2).setCellValue(plan.getArea());
            row.createCell(3).setCellValue(plan.getShopCode());
            row.createCell(4).setCellValue(plan.getShopName());
            row.createCell(5).setCellValue(plan.getPerformDate());
            row.createCell(6).setCellValue(plan.getAmounts().doubleValue());
            row.createCell(7).setCellValue(plan.getLastYearMonthAmounts().doubleValue());

            QueryWrapper<SalesPlanDetails> detailsQueryWrapper = new QueryWrapper<>();
            detailsQueryWrapper.eq("plan_id", plan.getId());
            List<SalesPlanDetails> detailsList = salesPlanDetailsService.list(detailsQueryWrapper);
            int rowCount = 4;
            Row detailsHeadRow = sheet.createRow( rowCount++);
            for (int l = 0; l < detailsColumnNames.length; l++) {
                detailsHeadRow.createCell(l).setCellValue(detailsColumnNames[l]);
            }
            for (int k = 0; k < detailsList.size(); k++) {
                SalesPlanDetails details = detailsList.get(k);

                Row detailsRow = sheet.createRow( rowCount++);
                detailsRow.createCell(0).setCellValue(details.getReasonType());
                detailsRow.createCell(1).setCellValue(details.getPrimaryReason());
                detailsRow.createCell(2).setCellValue(details.getReason());
                detailsRow.createCell(3).setCellValue(details.getProposalType());
                detailsRow.createCell(4).setCellValue(details.getProposal());
                detailsRow.createCell(5).setCellValue(details.getFeedback());
                detailsRow.createCell(6).setCellValue(details.getConfirmation());
//                detailsRow.createCell(5).setCellValue(details.getBeginDate());
                Cell beginDateCell = detailsRow.createCell(7);
                beginDateCell.setCellValue(details.getBeginDate());
                beginDateCell.setCellStyle(cellStyle);
                Cell endDateCell = detailsRow.createCell(8);
                endDateCell.setCellValue(details.getEndDate());
                endDateCell.setCellStyle(cellStyle);
//                detailsRow.createCell(6).setCellValue(details.getEndDate());
                detailsRow.createCell(9).setCellValue(details.getFrequency());
                detailsRow.createCell(10).setCellValue(details.getExecutor());
                detailsRow.createCell(11).setCellValue(details.getResult());

/*

                QueryWrapper<DetailsProposal> proposalQueryWrapper = new QueryWrapper<>();
                proposalQueryWrapper.eq("details_id", details.getId());
                List<DetailsProposal> proposals = detailsProposalService.list(proposalQueryWrapper);
                for (int m = 0; m < proposals.size(); m++) {
                    DetailsProposal proposal = proposals.get(m);
                    Row proposalRow = sheet.createRow( rowCount++);
                    proposalRow.createCell(0).setCellValue("开始日期");
                    Cell beginDateCell = proposalRow.createCell(1);
                    beginDateCell.setCellValue(proposal.getBeginDate());
                    beginDateCell.setCellStyle(cellStyle);
                    proposalRow.createCell(2).setCellValue("结束日期");
                    Cell endDateCell = proposalRow.createCell(3);
                    endDateCell.setCellValue(proposal.getBeginDate());
                    endDateCell.setCellStyle(cellStyle);
                    proposalRow.createCell(4).setCellValue("频率");
                    proposalRow.createCell(5).setCellValue(proposal.getFrequency());
                    proposalRow.createCell(6).setCellValue("方案");
                    proposalRow.createCell(7).setCellValue(proposal.getConfirmation());
                }*/
            }

        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode("业绩下降分析及行动方案跟进表.xlsx", "utf-8"));
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
