package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.PrivateTerminalHis;
import com.honji.order.entity.PublicTerminalHis;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DifferenceDTO;
import com.honji.order.model.TerminalDTO;
import com.honji.order.service.IPrivateTerminalHisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2021-07-31
 */
@Slf4j
@Controller
@RequestMapping("/private-terminal-his")
public class PrivateTerminalHisController {


    @Autowired
    private IPrivateTerminalHisService terminalService;


    @GetMapping("/index")
    public String index() {
        return "private-terminal-his";
    }

    @ResponseBody
    @GetMapping("/get")
    public PrivateTerminalHis get(@RequestParam String id) {
        return terminalService.getById(id);
    }

    @ResponseBody
    @GetMapping("/list")
    public DataGridResult list(TerminalDTO terminalDTO) {
        return new DataGridResult(terminalService.listForIndex(terminalDTO));
    }


    @ResponseBody
    @PostMapping("/save")
    public boolean save(@ModelAttribute PrivateTerminalHis terminal) {
        return terminalService.saveOrUpdate(terminal);
    }

    @ResponseBody
    @PostMapping("import")
    public boolean importExcel(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<PrivateTerminalHis> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum();
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();

            //循环除了所有行,如果要循环除第一行以外的就firstRowNum+1
            for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                Cell shopCodeCell = row.getCell(0);
                Cell bsPayCell = row.getCell(1);
                Cell yuePayCell = row.getCell(2);
                Cell unionSysCell = row.getCell(3);
                Cell unionPayCell = row.getCell(4);
                Cell topUpCell = row.getCell(5);
                Cell dateCell = row.getCell(6);

                String shopCode = shopCodeCell.getStringCellValue().trim();
                DataFormatter dataFormatter = new DataFormatter();
                String bsPay = dataFormatter.formatCellValue(bsPayCell);
                String yuePay = dataFormatter.formatCellValue(yuePayCell);
                String unionSys = dataFormatter.formatCellValue(unionSysCell);
                String unionPay = dataFormatter.formatCellValue(unionPayCell);
                String topUp = dataFormatter.formatCellValue(topUpCell);
                LocalDate createdDate = null;
                if(dateCell.getCellType() == CellType.STRING) {
                    String date = dataFormatter.formatCellValue(dateCell);
                    createdDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } else {
                    createdDate = dateCell.getLocalDateTimeCellValue().toLocalDate();
                }

                PrivateTerminalHis terminal = new PrivateTerminalHis().setKhdm(shopCode).setBsPay(bsPay).setYuePay(yuePay)
                        .setUnionSys(unionSys).setUnionPay(unionPay).setTopUp(topUp).setCreatedDate(createdDate);
                list.add(terminal);

            }
            long start = System.currentTimeMillis();

            try {
                result = terminalService.saveBatch(list);
            } catch (Exception e) {
                log.error("私户历史终端号 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("私户历史终端号 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("私户历史终端号 {} 导入失败", fileName);
            }

        }
        list = null;//释放list
        System.gc();
        return result;
    }


    @GetMapping("/export")
    @ResponseBody
    public void export(String date, HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");

        String columnNames[] = { "店铺代码", "百胜支付终端码", "悦支付终端码",
                "银联扫一扫终端码", "银联刷卡终端码", "充值终端码", "日期"};// 列名

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        QueryWrapper<PrivateTerminalHis> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("created_date", date);
        List<PrivateTerminalHis> terminals = terminalService.list(queryWrapper);
        for (int i = 0; i < terminals.size(); i++) {
            PrivateTerminalHis terminal = terminals.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(terminal.getKhdm());
            row.createCell(1).setCellValue(terminal.getBsPay());
            row.createCell(2).setCellValue(terminal.getYuePay());
            row.createCell(3).setCellValue(terminal.getUnionSys());
            row.createCell(4).setCellValue(terminal.getUnionPay());
            row.createCell(5).setCellValue(terminal.getTopUp());
            row.createCell(6).setCellValue(terminal.getCreatedDate().toString());
        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename="
                    + URLEncoder.encode("私户终端号".concat(date).concat(".xlsx"), "utf-8"));
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam List<String> ids) {
        return terminalService.removeByIds(ids);
    }

}
