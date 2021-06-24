package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.Bank;
import com.honji.order.entity.Bill;
import com.honji.order.enums.BillTypeEnum;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
import com.honji.order.service.IBankService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
@RequestMapping("/bank")
public class BankController {

    @Autowired
    private IBankService bankService;



    @GetMapping("/index")
    public String index() {

        return "bank";
    }

    @GetMapping("/get")
    @ResponseBody
    public Bank get(String id) {
        Bank bank = bankService.getById(id);
        return bank;
    }

    @GetMapping("/query")
    @ResponseBody
    public DataGridResult query(String account, int offset, int limit) {
        return new DataGridResult(bankService.listByAccount(account, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute Bank bank) {
        return bankService.saveOrUpdate(bank);
    }

    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam List<String> ids) {
        return bankService.removeByIds(ids);
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(String account, HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");

        String columnNames[] = { "银行账号", "银行名称", "到账编码", "类型"};// 列名
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        QueryWrapper<Bank> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(account)) {
            queryWrapper.like("account", account);
        }
        List<Bank> banks = bankService.list(queryWrapper);
        for (int i = 0; i < banks.size(); i++) {
            Bank bank = banks.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(bank.getAccount());
            row.createCell(1).setCellValue(bank.getName());
            row.createCell(2).setCellValue(bank.getCode());
            row.createCell(3).setCellValue(bank.getType());
        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("门店银行账号信息.xlsx", "utf-8"));
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
        List<Bank> list = new ArrayList<>();
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

                Cell accountCell = row.getCell(0);
                if (accountCell.getCellType() == CellType.BLANK) {//尾部数据直接跳出
                    break;
                }
                Cell nameCell = row.getCell(1);
                Cell codeCell = row.getCell(2);
                Cell typeCell = row.getCell(3);

                String account = accountCell.getStringCellValue().trim();
                String name = nameCell.getStringCellValue().trim();
                System.out.println("account==" + account);
                String code = codeCell.getStringCellValue().trim();
                int type = (int) typeCell.getNumericCellValue();

                Bank bank = new Bank();
                bank.setAccount(account).setName(name).setCode(code).setType(type);
                list.add(bank);

            }
            long start = System.currentTimeMillis();

            try {
                result = bankService.saveBatch(list);
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
