package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.PublicTerminalHis;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DifferenceDTO;
import com.honji.order.model.TerminalDTO;
import com.honji.order.service.IPublicTerminalHisService;
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
 * @since 2021-08-12
 */
@Slf4j
@Controller
@RequestMapping("/public-terminal-his")
public class PublicTerminalHisController {

    @Autowired
    private IPublicTerminalHisService terminalService;


    @GetMapping("/index")
    public String index() {
        return "public-terminal-his";
    }

    @ResponseBody
    @GetMapping("/get")
    public PublicTerminalHis get(@RequestParam String id) {
        return terminalService.getById(id);
    }

    @ResponseBody
    @GetMapping("/list")
    public DataGridResult list(TerminalDTO terminalDTO) {
        return new DataGridResult(terminalService.listForIndex(terminalDTO));
    }


    @ResponseBody
    @PostMapping("/save")
    public boolean save(@ModelAttribute PublicTerminalHis terminal) {
        return terminalService.saveOrUpdate(terminal);
    }

    @ResponseBody
    @PostMapping("import")
    public boolean importExcel(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<PublicTerminalHis> list = new ArrayList<>();
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
                Cell dateCell = row.getCell(4);

                String shopCode = shopCodeCell.getStringCellValue().trim();
                DataFormatter dataFormatter = new DataFormatter();
                String ccb = dataFormatter.formatCellValue(ccbCell);
                String pos = dataFormatter.formatCellValue(posCell);
                String topUp = dataFormatter.formatCellValue(topUpCell);
                LocalDate createdDate = null;
                if(dateCell.getCellType() == CellType.STRING) {
                    String date = dataFormatter.formatCellValue(dateCell);
                    createdDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } else {
                    createdDate = dateCell.getLocalDateTimeCellValue().toLocalDate();
                }

                //暂时不能使用构造方法，另一功能的sql查询会匹配到构造方法并报错
                PublicTerminalHis terminal = new PublicTerminalHis().setKhdm(shopCode)
                        .setCcb(ccb).setPos(pos).setTopUp(topUp).setCreatedDate(createdDate);
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
    public void export(String date, HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        String columnNames[] = { "店铺代码", "建行终端码", "浦发刷卡或银联刷卡终端码", "充值终端码", "日期"};// 列名

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        QueryWrapper<PublicTerminalHis> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("created_date", date);
        List<PublicTerminalHis> terminals = terminalService.list(queryWrapper);
        for (int i = 0; i < terminals.size(); i++) {
            PublicTerminalHis terminal = terminals.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(terminal.getKhdm());
            row.createCell(1).setCellValue(terminal.getCcb());
            row.createCell(2).setCellValue(terminal.getPos());
            row.createCell(3).setCellValue(terminal.getTopUp());
            row.createCell(4).setCellValue(terminal.getCreatedDate().toString());
        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("公户历史终端号.xlsx", "utf-8"));
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
