package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.honji.order.entity.Authority;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.CashDifference;
import com.honji.order.enums.DifferenceTypeEnum;
import com.honji.order.model.*;
import com.honji.order.service.IAuthorityService;
import com.honji.order.service.ICashBalanceService;
import com.honji.order.service.ICashDifferenceService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
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

        return new DataGridResult(differenceService.listForIndex(offset, limit, shopCode));
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
        System.out.println("===" + cashDifference.getType());
        if (cashDifference.getType() == DifferenceTypeEnum.CASH_SUPPLEMENT) {
            cashDifference.setActualAmount(BigDecimal.ZERO);//现金补单实收金额固定为0
        }
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

    @GetMapping("/export")
    @ResponseBody
    public void export(DifferenceDTO dto, HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");

        String columnNames[] = { "店铺代码", "店铺名称", "填写时间", "小票日期", "小票单号", "小票金额", "实收金额", "差额"};// 列名
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        List<DifferenceVO> differences = differenceService.listAll(dto);
        for (int i = 0; i < differences.size(); i++) {
            DifferenceVO difference = differences.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(difference.getShopCode());
            row.createCell(1).setCellValue(difference.getShopName());
            Cell createdTimeCell = row.createCell(2);
            Cell dateCell = row.createCell(3);
            createdTimeCell.setCellValue(difference.getCreatedTime());
            createdTimeCell.setCellStyle(cellStyle);
            dateCell.setCellValue(difference.getDate());
            dateCell.setCellStyle(cellStyle);

            row.createCell(4).setCellValue(difference.getNumber());
            row.createCell(5).setCellValue(difference.getAmount().doubleValue());
            row.createCell(6).setCellValue(difference.getActualAmount().doubleValue());
            row.createCell(7).setCellValue(difference.getDifference().doubleValue());

        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("现金差额数据.xlsx", "utf-8"));
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
