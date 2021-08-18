package com.honji.order.controller;


import com.honji.order.entity.PublicTerminal;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DifferenceDTO;
import com.honji.order.service.IPublicTerminalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2021-08-12
 */
@Slf4j
@Controller
@RequestMapping("/public-terminal")
public class PublicTerminalController {

    @Autowired
    private IPublicTerminalService terminalService;


    @GetMapping("/index")
    public String index() {
        return "public-terminal";
    }

    @ResponseBody
    @GetMapping("/get")
    public PublicTerminal get(@RequestParam String id) {
        return terminalService.getById(id);
    }

    @ResponseBody
    @GetMapping("/list")
    public DataGridResult list(@RequestParam(defaultValue = "0") int offset, @RequestParam int limit, String search) {
        return new DataGridResult(terminalService.listForIndex(offset, limit, search));
    }


    @ResponseBody
    @PostMapping("/save")
    public boolean save(@ModelAttribute PublicTerminal terminal) {
        return terminalService.saveOrUpdate(terminal);
    }

    @ResponseBody
    @PostMapping("import")
    public boolean importExcel(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<PublicTerminal> list = new ArrayList<>();
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
                Cell ccbCell = row.getCell(1);
                Cell posCell = row.getCell(2);
                Cell topUpCell = row.getCell(3);

                String shopCode = shopCodeCell.getStringCellValue().trim();

                DataFormatter dataFormatter = new DataFormatter();
                String ccb = dataFormatter.formatCellValue(ccbCell);
                String pos = dataFormatter.formatCellValue(posCell);
                String topUp = dataFormatter.formatCellValue(topUpCell);

                PublicTerminal terminal = new PublicTerminal(shopCode, ccb, pos, topUp);
                list.add(terminal);

            }
            long start = System.currentTimeMillis();

            try {
                result = terminalService.saveBatch(list);
            } catch (Exception e) {
                log.error("公户终端号 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("公户终端号 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("公户终端号 {} 导入失败", fileName);
            }

        }
        list = null;//释放list
        System.gc();
        return result;
    }


    @GetMapping("/export")
    @ResponseBody
    public void export(DifferenceDTO dto, HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        String columnNames[] = { "店铺代码", "建行终端码", "浦发刷卡或银联刷卡终端码", "充值终端码"};// 列名

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        List<PublicTerminal> terminals = terminalService.list();
        for (int i = 0; i < terminals.size(); i++) {
            PublicTerminal terminal = terminals.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(terminal.getKhdm());
            row.createCell(1).setCellValue(terminal.getCcb());
            row.createCell(2).setCellValue(terminal.getPos());
            row.createCell(3).setCellValue(terminal.getTopUp());
        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("公户终端号.xlsx", "utf-8"));
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
