package com.honji.order.controller;


import com.honji.order.entity.Bill;
import com.honji.order.enums.BillTypeEnum;
import com.honji.order.service.IBillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2020-04-20
 */
@Slf4j
@Controller
@RequestMapping("/import")
public class ImportController {


    @Autowired
    private IBillService billService;

    private DateTimeFormatter hyphenatedFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter unhyphenatedFormatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");

    @GetMapping("/index")
    public String index() {

        return null;
    }


    /**
     * 百胜支付
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("bs-pay")
    public boolean baiShengPay(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<Bill> list = new ArrayList<>();
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

                Cell dateCell = row.getCell(1);
                Cell timeCell = row.getCell(2);
                Cell terminalCell = row.getCell(3);
                Cell amountCell = row.getCell(6);
                Cell feeCell = row.getCell(8);
                Cell merchantCell = row.getCell(12);
                Cell orderCell = row.getCell(17);

                Cell tradeTypeCell = row.getCell(4);//交易类型
//                Cell remarkCell = row.getCell(19);//备注

                String time = String.valueOf(dateCell.getLocalDateTimeCellValue().toLocalDate())
                        .trim().concat(" ").concat(timeCell.getStringCellValue().trim());
                LocalDateTime date = LocalDateTime.parse(time, hyphenatedFormatter);
                String terminalNum;
                if (terminalCell.getCellType() == CellType.STRING) {
                    terminalNum = terminalCell.getStringCellValue().trim();
                } else { //不是string 就是Numeric
                    int num = (int) terminalCell.getNumericCellValue();
                    terminalNum = String.valueOf(num);
                }
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                String tradeType = tradeTypeCell.getStringCellValue().trim();
                //String remark = remarkCell.getStringCellValue();
                BillTypeEnum type = BillTypeEnum.BS_PAY;

                //扫一扫账单已经迁到百胜刷卡账单
                //如果备注字段为空并且交易类型为“消费” 则是扫一扫类型
//                if ((remarkCell == null || StringUtils.isBlank(remarkCell.getStringCellValue()))
//                        && "消费".equals(tradeType)) {
//                    type = BillTypeEnum.BS_SYS;
//                }

                Bill bill = new Bill(date, amount, fee, terminalNum,orderId,
                        merchant, null, type.getCode());
                list.add(bill);

            }
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("百胜支付 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("百胜支付 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("百胜支付 {} 导入失败", fileName);
            }

        }
        list = null;//释放list
        System.gc();
        return result;
    }

    /**
     * 悦支付
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/yue-pay")
    public boolean yuePay(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<Bill> list = new ArrayList<>();
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

                Cell dateCell = row.getCell(4);
                Cell timeCell = row.getCell(5);
                Cell terminalCell = row.getCell(7);
                Cell amountCell = row.getCell(10);
                Cell feeCell = row.getCell(12);
                //Cell merchantCell = row.getCell(12);
                Cell orderCell = row.getCell(6);

                String time = String.valueOf(dateCell.getStringCellValue()).trim().concat(" ")
                    .concat(timeCell.getStringCellValue().trim());
                //LocalDate date = dateCell.getLocalDateTimeCellValue().toLocalDate();

                LocalDateTime date = LocalDateTime.parse(time, hyphenatedFormatter);

                String terminalNum;
                if (terminalCell.getCellType() == CellType.STRING) {
                    terminalNum = terminalCell.getStringCellValue().trim();
                } else { //不是string 就是Numeric
                    int num = (int) terminalCell.getNumericCellValue();
                    terminalNum = String.valueOf(num);
                }
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                //String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                Bill bill = new Bill(date, amount, fee, terminalNum,
                        orderId, null, null, BillTypeEnum.YUE_PAY.getCode());
                list.add(bill);

            }
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("悦支付 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("悦支付 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("悦支付 {} 导入失败", fileName);
            }

        }
        list = null;//释放list
        System.gc();
        return result;
    }

    /**
     * 百胜刷卡
     * @param file
     * @return
     * @throws IOException
     */
   /* @ResponseBody
    @PostMapping("/bs-sk")
    public boolean bssk(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<Bill> list = new ArrayList<>();
        boolean result = false;

        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 4;//刷卡账单是从第4行开始
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            //循环除了所有行,如果要循环除第一行以外的就firstRowNum+1
            //前3行如果是手动制造空白行会被解析为无效行，而前3条有效记录会成为空行导致没有导入成功，所以需要原始账单，不能手动修改账单
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                Cell dateCell = row.getCell(0);//百胜刷卡刷卡认“清算日期”
                if (dateCell.getCellType() == CellType.BLANK) {//尾部数据直接跳出
                    break;
                }
                Cell timeCell = row.getCell(2);
                Cell terminalCell = row.getCell(4);
                Cell amountCell = row.getCell(5);
                Cell feeCell = row.getCell(7);
                Cell orderCell = row.getCell(9);
                Cell merchantCell = row.getCell(14);

                //百胜刷卡刷卡认“清算日期”,而清算日期没有时间，需要在交易时间列获取时间，但交易时间又包含了日期，所以需要拆分
                String timeVal = timeCell.getLocalDateTimeCellValue().toLocalTime().format(timeFormatter);
                String time = dateCell.getStringCellValue().trim().concat(" ").concat(timeVal);
                LocalDateTime date = LocalDateTime.parse(time, unhyphenatedFormatter);

                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                Bill bill = new Bill(date, amount, fee, terminalNum,
                        orderId, merchant, null, BillTypeEnum.BS_SK.getCode());
                list.add(bill);

            }
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("百胜刷卡 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("百胜刷卡 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("百胜刷卡 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }*/

    /**
     * 浦发刷卡
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/pf-sk")
    public boolean spdb(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<Bill> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 2;//浦发刷卡账单是从第3行开始
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了所有行,如果要循环除第一行以外的就firstRowNum+1
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                Cell dateCell = row.getCell(6);
                if (dateCell.getCellType() == CellType.BLANK) {//尾部数据直接跳出
                    break;
                }
                Cell timeCell = row.getCell(7);
                Cell terminalCell = row.getCell(2);
                Cell amountCell = row.getCell(15);
                Cell feeCell = row.getCell(16);
                Cell orderCell = row.getCell(10);
//                Cell merchantCell = row.getCell(3);
                Cell typeCell = row.getCell(8);
                String tradeType =  typeCell.getStringCellValue().trim();
                BillTypeEnum type = BillTypeEnum.PF_SK;//2是刷卡
                if ("扫码".equals(tradeType)) {//5是扫码
                    type = BillTypeEnum.PF_SM;
                }

                String time = dateCell.getStringCellValue().trim().concat(" ")
                        .concat(timeCell.getStringCellValue().trim());
                LocalDateTime date = LocalDateTime.parse(time, unhyphenatedFormatter);
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = Double.valueOf(amountCell.getStringCellValue());
                double fee = Double.valueOf(feeCell.getStringCellValue());
//                String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                Bill bill = new Bill(date, amount, fee, terminalNum,
                        orderId, null, null, type.getCode());
                list.add(bill);

            }
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("浦发刷卡 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("浦发刷卡 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("浦发刷卡 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }

    /**
     * 银联刷卡
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/union-pay")
    public boolean unionPay(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回

        List<Bill> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 1;//银联刷卡账单是从第2行开始
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了所有行,如果要循环除第一行以外的就firstRowNum+1
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                Cell dateCell = row.getCell(4);
                if (dateCell.getCellType() == CellType.BLANK) {//尾部数据直接跳出
                    break;
                }
                Cell timeCell = row.getCell(5);
                Cell terminalCell = row.getCell(7);
                Cell amountCell = row.getCell(10);
                Cell feeCell = row.getCell(12);
                Cell orderCell = row.getCell(15);
                Cell typeCell = row.getCell(3);//业务类型

                String time = dateCell.getStringCellValue().trim().concat(" ")
                        .concat(timeCell.getStringCellValue().trim());
                LocalDateTime date = LocalDateTime.parse(time, hyphenatedFormatter);
                //String time = String.valueOf(timeCell.getNumericCellValue());
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String typeStr = typeCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();
                BillTypeEnum type = BillTypeEnum.UNION_PAY;
                //如果业务类型为“POS通总部清算” 则是扫一扫类型
                if ("POS通总部清算".equals(typeStr)) {
                    type = BillTypeEnum.BS_SYS;
                }
                Bill bill = new Bill(date, amount, fee, terminalNum,
                        orderId, null, null, type.getCode());
                list.add(bill);

            }
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("银联刷卡 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("银联刷卡 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("银联刷卡 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }

    /**
     * 码上收
     * @param file
     * @return
     * @throws IOException
     */
    /*@ResponseBody
    @PostMapping("/bs-mss")
    public boolean mss(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<Bill> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 4;//码上收账单是从第4行开始
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了所有行,如果要循环除第一行以外的就firstRowNum
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                Cell dateCell = row.getCell(1);
                if (dateCell.getCellType() == CellType.BLANK) {//尾部数据直接跳出
                    break;
                }
                Cell timeCell = row.getCell(2);
                Cell terminalCell = row.getCell(4);
                Cell amountCell = row.getCell(3);
                Cell feeCell = row.getCell(6);
                Cell orderCell = row.getCell(15);

                LocalDateTime date = timeCell.getLocalDateTimeCellValue();
//                String time = timeCell.getLocalDateTimeCellValue().toString();
                //String time = String.valueOf(timeCell.getNumericCellValue());
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
//                String merchant = "汕头市金平区科尚服装店";
                String orderId = orderCell.getStringCellValue().trim();
                Bill bill = new Bill(date, amount, fee, terminalNum,
                        orderId, null, null, BillTypeEnum.BS_MSS.getCode());
//                BaiShengPay baiShengPay = new BaiShengPay(date, time, amount, fee, terminalNum, orderId, merchant,type);
                list.add(bill);

            }
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("码上收 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("码上收 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("码上收 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }
*/

    /**
     * 建行
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/ccb-sm")
    public boolean ccb(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<Bill> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 2;
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();

            //循环除了所有行,如果要循环除第一行以外的就firstRowNum
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                Cell tradeDateCell = row.getCell(0);//建行账单暂时认记账日期为准
//                Cell dateCell = row.getCell(1);//记账日期没有时间
                if (tradeDateCell.getCellType() == CellType.BLANK) {//尾部数据直接跳出
                    break;
                }
                Cell terminalCell = row.getCell(12);
                Cell amountCell = row.getCell(11);
                Cell feeCell = row.getCell(10);
                //用流水号作orderId, 因为如果出现退款订单号会重复
                Cell orderCell = row.getCell(2);

//                System.out.println(amountCell.getStringCellValue().trim());
//                System.out.println(tradeDateCell.getStringCellValue().trim());
//                String time = tradeDateCell.getStringCellValue().trim();
//                LocalDateTime date = tradeDateCell.getLocalDateTimeCellValue();
                LocalDateTime date = LocalDateTime.parse(tradeDateCell.getStringCellValue(), hyphenatedFormatter);
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = Double.valueOf(amountCell.getStringCellValue());
                double fee = Double.valueOf(feeCell.getStringCellValue());
                String orderId = orderCell.getStringCellValue().trim();
                BillTypeEnum type = BillTypeEnum.CCB_SM;
                if (orderId.length() == 22 ) {//22位订单号属于离线账单
                    type = BillTypeEnum.CCB_LX;
                }

                Bill bill = new Bill(date, amount, fee, terminalNum,
                        orderId, null, null, type.getCode());
                list.add(bill);

            }
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("建行 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("建行 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("建行 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }


/**
     * 合胜收款 Z754143专用
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/he-sheng")
    public boolean heSheng(@RequestParam MultipartFile file) throws IOException {

        boolean result = false;
        String fileName = file.getOriginalFilename();

        //Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        InputStreamReader is = new InputStreamReader(file.getInputStream(), "GBK");
        BufferedReader br = new BufferedReader(is);

        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(br);
        List<CSVRecord> recordList = ((CSVParser) records).getRecords();
        int endLine = recordList.size() - 1;
        List<Bill> list = new ArrayList<>();

        for(int i = 3; i < endLine; i++) {//从第4行开始
            CSVRecord record = recordList.get(i);
            String dateStr =  record.get(2).trim();
            LocalDateTime date = LocalDateTime.parse(dateStr.substring(2, dateStr.length() - 1), hyphenatedFormatter);
            String amountStr =  record.get(15).trim();
            double amount = Double.valueOf(amountStr.substring(2, amountStr.length() - 1));
            String feeStr =  record.get(17).trim();
            double fee = Double.valueOf(feeStr.substring(2, feeStr.length() - 1));
            String orderIdStr =  record.get(4).trim();
            String orderId = orderIdStr.substring(2, orderIdStr.length() - 1);
            final String khdm = "Z754143";
            //System.out.println(type);
            //退款业务订单号与消费订单号重复，用微信退款单号替换，为保证orderId唯一性
//            if ("REFUND".equals(type)) { //退款类型
//                amount = - Double.valueOf(record.get(16).trim().substring(1));//退款为负数
//                orderId = new String(record.get(14).trim().substring(1));
//                System.out.println(orderId);
//            }

            Bill bill = new Bill(date, amount, fee, null,
                    orderId, null, khdm, BillTypeEnum.HE_SHENG.getCode());
//            WxAliPay wxPay = new WxAliPay(date, amount, fee, orderId, khdm, 5);
            list.add(bill);
        }

        if(list.size() > 0) {
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("合胜收款 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("合胜收款 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("合胜收款 {} 导入失败", fileName);
            }
        } else {
            log.warn("合胜收款账单没有数据！");
        }

        list = null;//释放list
        System.gc();
        return result;
    }


    /**
     * 微信公户账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/wx-pay-public")
    public boolean publicWxpay(@RequestParam MultipartFile file) throws IOException {
        return wxpay(file, BillTypeEnum.WX_PAY_PUBLIC.getCode());
    }

     /**
     * 微信私户账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/wx-pay-private")
    public boolean privateWxpay(@RequestParam MultipartFile file) throws IOException {
        return wxpay(file, BillTypeEnum.ALI_PAY_PRIVATE.getCode());
    }

    private boolean wxpay(MultipartFile file, String orderType) throws IOException {
        boolean result = false;
        String fileName = file.getOriginalFilename();

        //Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        InputStreamReader is = new InputStreamReader(file.getInputStream());
        BufferedReader br = new BufferedReader(is);
        //FileReader fileReader = new FileReader(br);

        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(br);
        List<CSVRecord> recordList = ((CSVParser) records).getRecords();
        int endLine = recordList.size() - 2;
        List<Bill> list = new ArrayList<>();

        for(int i = 1; i < endLine; i++) {
            CSVRecord record = recordList.get(i);
            LocalDateTime date = LocalDateTime.parse(record.get(0).trim().substring(1), hyphenatedFormatter);
            double amount = Double.valueOf(record.get(24).trim().substring(1));
            double fee = Double.valueOf(record.get(22).trim().substring(1));
            String orderId = record.get(5).trim().substring(1);
            String khdm =  record.get(6).trim().substring(1).split("_")[0];
            String type = record.get(9).trim().substring(1);
            //System.out.println(type);
            //退款业务订单号与消费订单号重复，用微信退款单号替换，为保证orderId唯一性
            if ("REFUND".equals(type)) { //退款类型
                amount = - Double.valueOf(record.get(16).trim().substring(1));//退款为负数
                orderId = new String(record.get(14).trim().substring(1));
            }
            Bill bill = new Bill(date, amount, fee, null,
                    orderId, null, khdm, orderType);
//            WxAliPay wxPay = new WxAliPay(date, amount, fee, orderId, khdm, orderType);
            list.add(bill);
        }

        if(list.size() > 0) {
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("微信 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("微信 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("微信 {} 导入失败", fileName);
            }
        } else {
            log.warn("账单没有数据！");
        }

        list = null;//释放list
        System.gc();
        return result;
    }

    /**
     * 支付宝公户账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/ali-pay-public")
    public boolean publicAlipay(@RequestParam MultipartFile file) throws IOException {
        return alipay(file, BillTypeEnum.ALI_PAY_PUBLIC.getCode());
    }

    /**
     * 支付宝私户账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/ali-pay-private")
    public boolean privateAlipay(@RequestParam MultipartFile file) throws IOException {
        return alipay(file, BillTypeEnum.ALI_PAY_PRIVATE.getCode());
    }


    private boolean alipay(MultipartFile file, String orderType) throws IOException {

        boolean result = false;
        String fileName = file.getOriginalFilename();

        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        InputStreamReader is = new InputStreamReader(file.getInputStream(), "GBK");
        BufferedReader br = new BufferedReader(is);

        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(br);
        List<CSVRecord> recordList = ((CSVParser) records).getRecords();
        List<CSVRecord> qualifiedRecords = new ArrayList<>();
        int endLine = recordList.size() - 4;
        List<Bill> list = new ArrayList<>();

        for(int i = 5; i < endLine; i++) {
            CSVRecord record = recordList.get(i);
            String name = record.get(3).trim();//商品名称
            String type = record.get(10).trim();
            List<String> types = Arrays.asList("提现", "收费", "退费");
            //去除魔体的数据和类型为“提现”的数据
            if (!name.startsWith("当面付条码支付") ||
                    types.contains(type)) {
                continue;
            }
            qualifiedRecords.add(record);
        }

        /**支付宝账单一笔消费有2条流水记录，第1条是消费金额，第2条是手续费
         * 正常支付“收入金额”为消费金额，“支出金额”为手续费
         * 交易退款“支出金额”为消费金额，“收入金额”为手续费
         * 退款业务流水号与消费重复，用财务流水号替换，为保证orderId唯一性
         */
        for(int i = 0; i < qualifiedRecords.size(); i++) {
            CSVRecord record = qualifiedRecords.get(i);

            LocalDateTime date = LocalDateTime.parse(record.get(4).trim(), hyphenatedFormatter);
            double amount = Double.valueOf(record.get(6).trim());
//            double fee = - Double.valueOf(feeRecord.get(7).trim());//账单为负数加负号改为正数
            double fee = Double.parseDouble(String.format("%.2f", amount * 0.006));//手续费固定0.006，四舍五入保留两位小数
            String orderId = record.get(1).trim();
            String shopCode =  record.get(2).trim().split("_")[0];
            String type = record.get(10).trim();

            if ("交易退款".equals(type) || "退款（交易退款）".equals(type)) { //退款类型
                amount = Double.valueOf(record.get(7).trim());
                fee = Double.parseDouble(String.format("%.2f", amount * 0.006));//退款手续费为负数
                orderId = new String(record.get(0).trim());
            }

            Bill bill = new Bill(date, amount, fee, null,
                    orderId, null, shopCode, orderType);
            list.add(bill);
        }

        if(list.size() > 0) {
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("支付宝 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("支付宝 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("支付宝 {} 导入失败", fileName);
            }
        } else {
            log.warn("支付宝 {} 账单没有数据！", fileName);
        }

        list = null;//释放list
        System.gc();
        return result;
    }


    /**
     * 充值账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/top-up")
    public boolean topUp(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<Bill> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 4;//刷卡账单是从第4行开始
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //循环除了所有行,如果要循环除第一行以外的就firstRowNum+1
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                Cell dateCell = row.getCell(1);
                if (dateCell.getCellType() == CellType.BLANK) {//尾部数据直接跳出
                    break;
                }
                Cell timeCell = row.getCell(2);
                Cell terminalCell = row.getCell(4);
                Cell amountCell = row.getCell(3);
                Cell feeCell = row.getCell(6);
                Cell orderCell = row.getCell(15);
                Cell merchantCell = row.getCell(14);

                LocalDateTime date = timeCell.getLocalDateTimeCellValue();
//                String time = timeCell.getLocalDateTimeCellValue().toString();
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                Bill bill = new Bill(date, amount, fee, terminalNum,
                        orderId, merchant, null, BillTypeEnum.TOP_UP.getCode());
//                BaiShengSwipe baiShengSwipe = new BaiShengSwipe(date, time, amount, fee, terminalNum, orderId, merchant,4);
                list.add(bill);

            }
            long start = System.currentTimeMillis();

            try {
                result = billService.saveBatch(list);
            } catch (Exception e) {
                log.error("充值账单 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("充值账单 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("充值账单 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }

}
