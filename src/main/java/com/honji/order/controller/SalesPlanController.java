package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.DetailsProposal;
import com.honji.order.entity.SalesPlan;
import com.honji.order.entity.SalesPlanDetails;
import com.honji.order.entity.Shop;
import com.honji.order.entity.vo.SalesPlanVO;
import com.honji.order.mapper.AreaMapper;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.IDetailsProposalService;
import com.honji.order.service.ISalesPlanDetailsService;
import com.honji.order.service.ISalesPlanService;
import lombok.extern.slf4j.Slf4j;
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
    private AreaMapper areaMapper;


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
    public void export(HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();


        String columnNames[] = { "制表人", "制表日期", "销售区域", "店铺编码", "店铺名称", "业绩分析时点",
                "当月业绩", "同期业绩"};// 列名
        String detailsColumnNames[] = { "原因类型", "主要原因", "原因分析", "常规方案", "创新方案", "大区经理反馈",
                "执行人", "结果"};// 原因列名
//        String proposalColumnNames[] = { "方案", "日期", "原因分析", "常规方案", "创新方案", "大区经理反馈",
//                "执行人", "结果"};// 方案列名
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));


        List<SalesPlanVO> plans = salesPlanService.listForExport();
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
//            row.createCell(8).setCellValue(deposit.getCcbZs());
//            row.createCell(9).setCellValue(deposit.getCcbBs());
//            row.createCell(10).setCellValue(deposit.getAlipay());
//            row.createCell(11).setCellValue(deposit.getWxpay());
//            row.createCell(12).setCellValue(deposit.getSys());
//            row.createCell(13).setCellValue(deposit.getMss());
//            row.createCell(14).setCellValue(deposit.getBsPay());
//            row.createCell(15).setCellValue(deposit.getMallCollection());
//            row.createCell(16).setCellValue(deposit.getHeSheng());
//            row.createCell(17).setCellValue(deposit.getCash());
//            row.createCell(18).setCellValue(deposit.getMemberPoints());
//            row.createCell(19).setCellValue(deposit.getCardConsumption());
//            row.createCell(20).setCellValue(deposit.getGiftCertificate());
            QueryWrapper<SalesPlanDetails> detailsQueryWrapper = new QueryWrapper<>();
            detailsQueryWrapper.eq("plan_id", plan.getId());
            List<SalesPlanDetails> detailsList = salesPlanDetailsService.list(detailsQueryWrapper);
            int rowCount = 4;
            for (int k = 0; k < detailsList.size(); k++) {

                SalesPlanDetails details = detailsList.get(k);
                Row detailsHeadRow = sheet.createRow( rowCount++);
                for (int l = 0; l < detailsColumnNames.length; l++) {
                    detailsHeadRow.createCell(l).setCellValue(detailsColumnNames[l]);
                }
                Row detailsRow = sheet.createRow( rowCount++);
                detailsRow.createCell(0).setCellValue(details.getReasonType());
                detailsRow.createCell(1).setCellValue(details.getPrimaryReason());
                detailsRow.createCell(2).setCellValue(details.getReason());
                detailsRow.createCell(3).setCellValue(details.getConvention());
                detailsRow.createCell(4).setCellValue(details.getInnovation());
                detailsRow.createCell(5).setCellValue(details.getFeedback());
                detailsRow.createCell(6).setCellValue(details.getExecutor());
                detailsRow.createCell(7).setCellValue(details.getResult());


                QueryWrapper<DetailsProposal> proposalQueryWrapper = new QueryWrapper<>();
                proposalQueryWrapper.eq("details_id", details.getId());
                List<DetailsProposal> proposals = detailsProposalService.list(proposalQueryWrapper);
                for (int m = 0; m < proposals.size(); m++) {
                    DetailsProposal proposal = proposals.get(m);
                    Row proposalRow = sheet.createRow( rowCount++);
                    proposalRow.createCell(0).setCellValue("日期");
                    Cell proposalDateCell = proposalRow.createCell(1);
                    proposalDateCell.setCellValue(proposal.getDate());
                    proposalDateCell.setCellStyle(cellStyle);
                    proposalRow.createCell(2).setCellValue("方案");
                    proposalRow.createCell(3).setCellValue(proposal.getConfirmation());
                }
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
