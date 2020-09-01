package com.honji.order.controller;


import com.honji.order.entity.BaiShengPay;
import com.honji.order.entity.BaiShengSwipe;
import com.honji.order.entity.CcbOrder;
import com.honji.order.entity.WxAliPay;
import com.honji.order.service.IBaiShengPayService;
import com.honji.order.service.IBaiShengSwipeService;
import com.honji.order.service.ICcbOrderService;
import com.honji.order.service.IWxAliPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
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
    private IBaiShengPayService baiShengPayService;

    @Autowired
    private IBaiShengSwipeService baiShengSwipeService;

    @Autowired
    private ICcbOrderService ccbOrderService;

    @Autowired
    private IWxAliPayService wxPayService;

    @GetMapping("/index")
    public String index() {

        return "index";
    }

        /**
         * 百胜支付
         * @param file
         * @return
         * @throws IOException
         */
    @ResponseBody
    @PostMapping("/bs-pay")
    public boolean baiShengPay(@RequestParam("bs-pay") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<BaiShengPay> list = new ArrayList<>();
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
                Cell remarkCell = row.getCell(19);//备注

                LocalDate date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                String time = timeCell.getStringCellValue();
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
                byte type = 1;

                if (remarkCell == null && "消费".equals(tradeType)) {//扫一扫类型
                    type = 2;
                }

                BaiShengPay baiShengPay = new BaiShengPay(date, time, amount, fee, terminalNum, orderId, merchant, type);
                list.add(baiShengPay);

            }
            long start = System.currentTimeMillis();

            try {
                result = baiShengPayService.saveBatch(list);
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
     * 百胜刷卡
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/bssk")
    public boolean bssk(@RequestParam("bssk") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<BaiShengSwipe> list = new ArrayList<>();
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
                Cell amountCell = row.getCell(5);
                Cell feeCell = row.getCell(7);
                Cell orderCell = row.getCell(9);
                Cell merchantCell = row.getCell(14);

                LocalDate date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                String time = timeCell.getLocalDateTimeCellValue().toString();
                //String time = String.valueOf(timeCell.getNumericCellValue());
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                BaiShengSwipe baiShengSwipe = new BaiShengSwipe(date, time, amount, fee, terminalNum, orderId, merchant,1);
                list.add(baiShengSwipe);

            }
            long start = System.currentTimeMillis();

            try {
                result = baiShengSwipeService.saveBatch(list);
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
    }

    /**
     * 浦发刷卡
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/spdb")
    public boolean spdb(@RequestParam("spdb") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<BaiShengSwipe> list = new ArrayList<>();
        boolean result = false;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
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
                Cell merchantCell = row.getCell(3);
                Cell typeCell = row.getCell(8);
                String tradeType =  typeCell.getStringCellValue().trim();
                int type = 2;//2是刷卡
                if ("扫码".equals(tradeType)) {//5是扫码
                    type = 5;
                }

                LocalDate date = LocalDate.parse(dateCell.getStringCellValue().trim(), dtf);
                String time = timeCell.getStringCellValue().trim();
                //String time = String.valueOf(timeCell.getNumericCellValue());
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = Double.valueOf(amountCell.getStringCellValue());
                double fee = Double.valueOf(feeCell.getStringCellValue());
                String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                BaiShengSwipe baiShengSwipe = new BaiShengSwipe(date, time, amount, fee, terminalNum, orderId, merchant, type);
                list.add(baiShengSwipe);

            }
            long start = System.currentTimeMillis();

            try {
                result = baiShengSwipeService.saveBatch(list);
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
    public boolean unionPay(@RequestParam("union-pay") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<BaiShengSwipe> list = new ArrayList<>();
        boolean result = false;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
                Cell merchantCell = row.getCell(2);

                LocalDate date = LocalDate.parse(String.valueOf(dateCell.getStringCellValue()).trim(), dtf);
                String time = timeCell.getStringCellValue().trim();
                //String time = String.valueOf(timeCell.getNumericCellValue());
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                BaiShengSwipe baiShengSwipe = new BaiShengSwipe(date, time, amount, fee, terminalNum, orderId, merchant, 3);
                list.add(baiShengSwipe);

            }
            long start = System.currentTimeMillis();

            try {
                result = baiShengSwipeService.saveBatch(list);
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
    @ResponseBody
    @PostMapping("/mss")
    public boolean mss(@RequestParam("mss") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<BaiShengPay> list = new ArrayList<>();
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
                //Cell merchantCell = row.getCell(14);

                LocalDate date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                String time = timeCell.getLocalDateTimeCellValue().toString();
                //String time = String.valueOf(timeCell.getNumericCellValue());
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = "汕头市金平区科尚服装店";
                String orderId = orderCell.getStringCellValue().trim();
                byte type = 3;
                BaiShengPay baiShengPay = new BaiShengPay(date, time, amount, fee, terminalNum, orderId, merchant,type);
                list.add(baiShengPay);

            }
            long start = System.currentTimeMillis();

            try {
                result = baiShengPayService.saveBatch(list);
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


    /**
     * 建行
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/ccb")
    public boolean ccb(@RequestParam("ccb") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<CcbOrder> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 2;
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
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
                Cell terminalCell = row.getCell(12);
                Cell amountCell = row.getCell(11);
                Cell feeCell = row.getCell(10);
                Cell numCell = row.getCell(2);
                Cell orderCell = row.getCell(4);

                //int dateNum = (int) dateCell.getNumericCellValue();
                LocalDate date = LocalDate.parse(dateCell.getStringCellValue().trim(), dtf);
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = Double.valueOf(amountCell.getStringCellValue());
                double fee = Double.valueOf(feeCell.getStringCellValue());
                String num = numCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();
                int type = 1;
                if (orderId.length() == 22 ) {//22位订单号属于离线账单
                    type = 2;
                }

                CcbOrder ccbOrder = new CcbOrder(terminalNum, date, amount, fee, num, orderId, type);
                list.add(ccbOrder);

            }
            long start = System.currentTimeMillis();

            try {
                result = ccbOrderService.saveBatch(list);
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
     * 微信公户账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/public-wxpay")
    public boolean publicWxpay(@RequestParam("public-wxpay") MultipartFile file) throws IOException {
        return wxpay(file, 1);
    }

     /**
     * 微信私户账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/private-wxpay")
    public boolean privateWxpay(@RequestParam("private-wxpay") MultipartFile file) throws IOException {
        return wxpay(file, 2);
    }

    private boolean wxpay(MultipartFile file, int orderType) throws IOException {
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
        List<WxAliPay> list = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for(int i = 1; i < endLine; i++) {
            CSVRecord record = recordList.get(i);
            LocalDate date = LocalDate.parse(record.get(0).trim().substring(1), dtf);
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
                System.out.println(orderId);
            }
            //System.out.println(orderId.trim());
            WxAliPay wxPay = new WxAliPay(date, amount, fee, orderId, khdm, orderType);
            list.add(wxPay);
        }

        if(list.size() > 0) {
            long start = System.currentTimeMillis();

            try {
                result = wxPayService.saveBatch(list);
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
    @PostMapping("/public-alipay")
    public boolean publicAlipay(@RequestParam("public-alipay") MultipartFile file) throws IOException {
        return alipay(file, 3);
    }

    /**
     * 支付宝私户账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("/private-alipay")
    public boolean privateAlipay(@RequestParam("private-alipay") MultipartFile file) throws IOException {
        return alipay(file, 4);
    }


    private boolean alipay(MultipartFile file, int orderType) throws IOException {

        boolean result = false;
        String fileName = file.getOriginalFilename();

        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        InputStreamReader is = new InputStreamReader(file.getInputStream(), "GBK");
        BufferedReader br = new BufferedReader(is);

        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(br);
        List<CSVRecord> recordList = ((CSVParser) records).getRecords();
        List<CSVRecord> qualifiedRecords = new ArrayList<>();
        int endLine = recordList.size() - 4;
        List<WxAliPay> list = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

            LocalDate date = LocalDate.parse(record.get(4).trim(), dtf);
            double amount = Double.valueOf(record.get(6).trim());
//            double fee = - Double.valueOf(feeRecord.get(7).trim());//账单为负数加负号改为正数
            double fee = Double.parseDouble(String.format("%.2f", amount * 0.006));//手续费固定0.006，四舍五入保留两位小数
            String orderId = record.get(1).trim();
            String khdm =  record.get(2).trim().split("_")[0];
            String type = record.get(10).trim();
            //System.out.println(type);
            if ("交易退款".equals(type) || "退款（交易退款）".equals(type)) { //退款类型
                amount = Double.valueOf(record.get(7).trim());
                fee = Double.parseDouble(String.format("%.2f", amount * 0.006));//退款手续费为负数
                orderId = new String(record.get(0).trim());
                System.out.println("===" + orderId);
            }
            WxAliPay wxPay = new WxAliPay(date, amount, fee, orderId, khdm, orderType);
            list.add(wxPay);
        }

        if(list.size() > 0) {
            long start = System.currentTimeMillis();

            try {
                result = wxPayService.saveBatch(list);
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
            log.warn("账单没有数据！");
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
    public boolean topUp(@RequestParam("top-up") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<BaiShengSwipe> list = new ArrayList<>();
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

                LocalDate date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                String time = timeCell.getLocalDateTimeCellValue().toString();
                //String time = String.valueOf(timeCell.getNumericCellValue());
                String terminalNum = terminalCell.getStringCellValue().trim();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = merchantCell.getStringCellValue().trim();
                String orderId = orderCell.getStringCellValue().trim();

                BaiShengSwipe baiShengSwipe = new BaiShengSwipe(date, time, amount, fee, terminalNum, orderId, merchant,4);
                list.add(baiShengSwipe);

            }
            long start = System.currentTimeMillis();

            try {
                result = baiShengSwipeService.saveBatch(list);
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
