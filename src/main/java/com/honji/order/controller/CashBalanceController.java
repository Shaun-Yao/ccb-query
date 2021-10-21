package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.Authority;
import com.honji.order.entity.Bank;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.enums.ShopTypeEnum;
import com.honji.order.model.BalanceDTO;
import com.honji.order.model.BalanceVO;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DepositDTO;
import com.honji.order.service.IAuthorityService;
import com.honji.order.service.ICashBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
@Slf4j
@Controller
@RequestMapping("/cash-balance")
public class CashBalanceController {

    @Autowired
    private ICashBalanceService cashBalanceService;


    @Autowired
    private IAuthorityService authorityService;



    @GetMapping("/calculate")
    @ResponseBody
    public double calculate(@RequestParam String shopCode) {
        return cashBalanceService.calBalance(shopCode);
    }

    @GetMapping("/index")
    public String index() {

        return "cash-balance";
    }

    @GetMapping("/get")
    @ResponseBody
    public CashBalance get(String id) {
        CashBalance cashBalance = cashBalanceService.getById(id);
        return cashBalance;
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(String shopCode, int offset, int limit) {
        return new DataGridResult(cashBalanceService.listByShopCode(shopCode, offset, limit));
    }

    @GetMapping("/to-query")
    public String query(@RequestParam String jobNum, Model model) {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_num", jobNum);
        List<Authority> authorities = authorityService.list(queryWrapper);
        model.addAttribute("authorities", authorities);
        return "balance-query";
    }

    @GetMapping("/query")
    @ResponseBody
    public DataGridResult query(BalanceDTO balanceDTO) {
        return new DataGridResult(cashBalanceService.query(balanceDTO));
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute CashBalance cashBalance) {
        return cashBalanceService.saveOrUpdate(cashBalance);
    }

    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam List<String> ids) {
        return cashBalanceService.removeByIds(ids);
    }

    @GetMapping("/exportBalance")
    @ResponseBody
    public void exportBalance(BalanceDTO dto, HttpServletResponse response) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");

        String columnNames[] = { "店铺代码", "店铺名称", "结余", "日期"};// 列名
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        List<BalanceVO> balances = cashBalanceService.listAll(dto);
        for (int i = 0; i < balances.size(); i++) {
            BalanceVO balance = balances.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(balance.getShopCode());
            row.createCell(1).setCellValue(balance.getShopName());
            row.createCell(2).setCellValue(balance.getTotal().doubleValue());
            row.createCell(3).setCellValue(dto.getDate());
        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(
                    "门店现金结余".concat(dto.getDate()).concat(".xlsx"), "utf-8"));
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(String shopCode, HttpServletResponse response) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");

        String columnNames[] = { "店铺代码", "店铺名称", "店铺类型", "结余", "部门代码",
                "充值到账科目编码", "充值未到账科目编码", "私户到账科目编码", "私户未到账科目编码"};// 列名
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        QueryWrapper<CashBalance> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(shopCode)) {
            queryWrapper.like("khdm", shopCode);
        }
        List<CashBalance> cashBalances = cashBalanceService.list(queryWrapper);
        for (int i = 0; i < cashBalances.size(); i++) {
            CashBalance balance = cashBalances.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(balance.getKhdm());
            row.createCell(1).setCellValue(balance.getKhmc());
            row.createCell(2).setCellValue(balance.getType().getDesc());
            row.createCell(3).setCellValue(balance.getBalance());
            row.createCell(4).setCellValue(balance.getDeptCode());
            row.createCell(5).setCellValue(balance.getCzdzCode());
            row.createCell(6).setCellValue(balance.getCzwdzCode());
            row.createCell(7).setCellValue(balance.getShdzCode());
            row.createCell(8).setCellValue(balance.getShwdzCode());
        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(
                    "门店信息".concat(LocalDate.now().toString()).concat(".xlsx"), "utf-8"));
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @PostMapping("/import")
    public boolean import2(@RequestParam MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<CashBalance> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {
            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了所有行,如果要循环除第一行以外的就firstRowNum+1
            for (int rowNum = 1; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                Cell shopCodeCell = row.getCell(0);
                if (shopCodeCell.getCellType() == CellType.BLANK) {//尾部数据直接跳出
                    break;
                }
                Cell nameCell = row.getCell(1);
                Cell typeCell = row.getCell(2);
                Cell balanceCell = row.getCell(3);
                Cell deptCodeCell = row.getCell(4);
                Cell czdzCell = row.getCell(5);
                Cell czwdzCell = row.getCell(6);
                Cell shdzCell = row.getCell(7);
                Cell shwdzCell = row.getCell(8);

                DataFormatter dataFormatter = new DataFormatter();
                String khdm = dataFormatter.formatCellValue(shopCodeCell).trim();
                String khmc = dataFormatter.formatCellValue(nameCell).trim();
                double balance = balanceCell.getNumericCellValue();
                String deptCode = dataFormatter.formatCellValue(deptCodeCell).trim();
                String czdz = null;
                String czwdz = null;
                String shdz = null;
                String shwdz = null;
                String type = typeCell.getStringCellValue();

                if (czdzCell != null) {
                    czdz = dataFormatter.formatCellValue(czdzCell).trim();
                }
                if (czwdzCell != null) {
                    czwdz = dataFormatter.formatCellValue(czwdzCell).trim();
                }
                if (shdzCell != null) {
                    shdz = dataFormatter.formatCellValue(shdzCell).trim();
                }
                if (shwdzCell != null) {
                    shwdz = dataFormatter.formatCellValue(shwdzCell).trim();
                }


                CashBalance cashBalance = new CashBalance();
                cashBalance.setKhdm(khdm).setKhmc(khmc).setType(ShopTypeEnum.findByDesc(type)).setBalance(balance)
                .setDeptCode(deptCode).setCzdzCode(czdz).setCzwdzCode(czwdz).setShdzCode(shdz).setShwdzCode(shwdz);
                list.add(cashBalance);

            }
            long start = System.currentTimeMillis();

            try {
                result = cashBalanceService.saveBatch(list);
            } catch (Exception e) {
                log.error("门店银行信息 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("门店银行信息 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("门店银行信息 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }
}
