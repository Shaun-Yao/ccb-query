package com.honji.order.controller;


import com.honji.order.entity.DouyinOrder;
import com.honji.order.entity.DouyinQuotation;
import com.honji.order.entity.FundBill;
import com.honji.order.entity.LiveQuotation;
import com.honji.order.model.DaRenVO;
import com.honji.order.service.IDouyinOrderService;
import com.honji.order.service.IDouyinQuotationService;
import com.honji.order.service.IFundBillService;
import com.honji.order.service.ILiveQuotationService;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2022-08-25
 */
@Slf4j
@Controller
@RequestMapping("/douyin-order")
public class DouyinOrderController {

    @Autowired
    private IDouyinOrderService orderService;

    @Autowired
    private IFundBillService fundBillService;

    @Autowired
    private IDouyinQuotationService douyinQuotationService;

    @Autowired
    private ILiveQuotationService liveQuotationService;


    @GetMapping("index")
    public String index() {
        return "daren-index";
    }

    @ResponseBody
    @PostMapping("douyin")
    public boolean douyin(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
//        Workbook workbook = WorkbookFactory.create(file.getInputStream());

        InputStream is = new FileInputStream(convFile);
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);            // InputStream or File for XLSX file (required)
//        Workbook workbook = WorkbookFactory.create(convFile);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<DouyinOrder> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println(sheet.getSheetName());
            DataFormatter df = new DataFormatter();
            int i = 0;//行数
            for (Row row : sheet) {
                if (i == 0) {//第1行跳过
                    i++;
                    continue;
                }
                if (row == null) {
                    break;
                }

                String orderNumber = row.getCell(0).getStringCellValue().trim();
                String subOrderNumber = row.getCell(1).getStringCellValue().trim();
                String paymentMethod = row.getCell(2).getStringCellValue().trim();
                String goodsName = row.getCell(3).getStringCellValue().trim();
                String goodsId = row.getCell(4).getStringCellValue().trim();
                int quantity = (int) row.getCell(5).getNumericCellValue();
                double price = row.getCell(6).getNumericCellValue();
                String submitTime = row.getCell(7).getStringCellValue().trim();
//                LocalDateTime submitTime = LocalDateTime.parse(time, unhyphenatedFormatter);
                String finishTime = df.formatCellValue(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));

//                String message = df.formatCellValue(row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                Cell messageCell = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String message = null;
                if (messageCell.getCellType() == CellType.STRING) {
                    message = messageCell.getStringCellValue().trim();
                } else { //不是string 就是Numeric
                    int num = (int) messageCell.getNumericCellValue();
                    message = String.valueOf(num);
                }
                String color = row.getCell(10).getStringCellValue().trim();
//                String remark = df.formatCellValue(row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                Cell remarkCell = row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String remark = null;
                if (remarkCell.getCellType() == CellType.STRING) {
                    remark = remarkCell.getStringCellValue().trim();
                } else { //不是string 就是Numeric
                    int num = (int) remarkCell.getNumericCellValue();
                    remark = String.valueOf(num);
                }
                String appChannel = row.getCell(12).getStringCellValue().trim();
                String status = row.getCell(13).getStringCellValue().trim();
                String cancelReason = df.formatCellValue(row.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String afterSalesStatus = df.formatCellValue(row.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String orderType = "douyin"; //row.getCell(16).getStringCellValue().trim();
                double totalAmount = row.getCell(17).getNumericCellValue();
                double freight = row.getCell(18).getNumericCellValue();
                double totalDiscount = row.getCell(19).getNumericCellValue();
                String platformOffers = df.formatCellValue(row.getCell(20, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String merchantOffers = df.formatCellValue(row.getCell(21, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String darenOffers = df.formatCellValue(row.getCell(22, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                double changePrice = row.getCell(23).getNumericCellValue();
                double paymentDiscount = row.getCell(24).getNumericCellValue();
                double fee = row.getCell(25).getNumericCellValue();
                double redEnvelope = row.getCell(26).getNumericCellValue();
                String expressInformation = df.formatCellValue(row.getCell(27, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String darenId = df.formatCellValue(row.getCell(28, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String darenNickName = df.formatCellValue(row.getCell(29, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String adChannel = df.formatCellValue(row.getCell(30, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));

                DouyinOrder order = new DouyinOrder( orderNumber, subOrderNumber, paymentMethod, goodsName, goodsId,
                        quantity, price, submitTime, finishTime, message, color, remark, appChannel, status,
                        cancelReason, afterSalesStatus, orderType, totalAmount, freight, totalDiscount,
                        platformOffers, merchantOffers, darenOffers, changePrice, paymentDiscount, fee, redEnvelope,
                        expressInformation, darenId, darenNickName, adChannel);
                list.add(order);

            }
            /*
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 1;
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            System.out.println(lastRowNum);
            DataFormatter df = new DataFormatter();
            Row row = null;
            //循环除了所有行,从第5行开始
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

                String orderNumber = row.getCell(0).getStringCellValue().trim();
                String subOrderNumber = row.getCell(1).getStringCellValue().trim();
                String paymentMethod = row.getCell(2).getStringCellValue().trim();
                String goodsName = row.getCell(3).getStringCellValue().trim();
                String goodsId = row.getCell(4).getStringCellValue().trim();
                int quantity = (int) row.getCell(5).getNumericCellValue();
                double price = row.getCell(6).getNumericCellValue();
                String submitTime = row.getCell(7).getStringCellValue().trim();
//                LocalDateTime submitTime = LocalDateTime.parse(time, unhyphenatedFormatter);
                String finishTime = df.formatCellValue(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));

                String message = df.formatCellValue(row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String color = row.getCell(10).getStringCellValue().trim();
                String remark = df.formatCellValue(row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String appChannel = row.getCell(12).getStringCellValue().trim();
                String status = row.getCell(13).getStringCellValue().trim();
                String cancelReason = df.formatCellValue(row.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String afterSalesStatus = df.formatCellValue(row.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String orderType = row.getCell(16).getStringCellValue().trim();
                double totalAmount = row.getCell(17).getNumericCellValue();
                double freight = row.getCell(18).getNumericCellValue();
                double totalDiscount = row.getCell(19).getNumericCellValue();
                String platformOffers = df.formatCellValue(row.getCell(20, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String merchantOffers = df.formatCellValue(row.getCell(21, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String darenOffers = df.formatCellValue(row.getCell(22, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                double changePrice = row.getCell(23).getNumericCellValue();
//                String  = row.getCell().getStringCellValue().trim();
//                String  = row.getCell().getStringCellValue().trim();

                DouyinOrder order = new DouyinOrder( orderNumber, subOrderNumber, paymentMethod, goodsName, goodsId,
                        quantity, price, submitTime, finishTime, message, color, remark, appChannel, status,
                        cancelReason, afterSalesStatus, orderType, totalAmount, freight, totalDiscount,
                        platformOffers, merchantOffers, darenOffers, changePrice, 0.0, 0.0, 0.0,
                        "expressInformation", "darenId", "darenNickName", "adChannel");
                list.add(order);

            }
            */
            long start = System.currentTimeMillis();

            try {
                result = orderService.saveBatch(list);
            } catch (Exception e) {
                log.error("抖音订单 {} 导入出现异常 {}", fileName, e.getMessage());
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("抖音订单 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("抖音订单 {} 导入失败", fileName);
            }

        }
        list = null;//释放list
        System.gc();
        return result;
    }


    @ResponseBody
    @PostMapping("kuaishou")
    public boolean kuaishou(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        InputStream is = new FileInputStream(convFile);
        Workbook workbook = StreamingReader.builder() //.password("73e5df")
                .rowCacheSize(1000)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);            // InputStream or File for XLSX file (required)
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<DouyinOrder> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter df = new DataFormatter();
            int i = 0;//行数
            for (Row row : sheet) {
                if (i == 0) {//第1行跳过
                    i++;
                    continue;
                }
                if (row == null) {
                    break;
                }

                String orderNumber = row.getCell(0).getStringCellValue().trim();
                String submitTime = row.getCell(1).getStringCellValue().trim();
                String finishTime = row.getCell(2).getStringCellValue().trim();
                String status = row.getCell(3).getStringCellValue().trim();
                double totalAmount = Double.parseDouble(row.getCell(4).getStringCellValue().trim().substring(1));
                double freight = Double.parseDouble(row.getCell(5).getStringCellValue().trim().substring(1));
                String goodsId = row.getCell(6).getStringCellValue().trim();
                String goodsName = row.getCell(7).getStringCellValue().trim();
                String orderType = "kuaishou";
                String darenId = df.formatCellValue(row.getCell(14, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String darenNickName = df.formatCellValue(row.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                int quantity = (int) row.getCell(19).getNumericCellValue();
                double price = Double.parseDouble(row.getCell(20).getStringCellValue().trim().substring(1));

                String expressCorporation = df.formatCellValue(row.getCell(27, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String expressNumber = df.formatCellValue(row.getCell(28, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String expressInformation = expressCorporation.concat(expressNumber);

/*
                int quantity = (int) row.getCell(5).getNumericCellValue();
                double price = row.getCell(6).getNumericCellValue();
                String finishTime = df.formatCellValue(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));

                Cell messageCell = row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String message = null;
                if (messageCell.getCellType() == CellType.STRING) {
                    message = messageCell.getStringCellValue().trim();
                } else { //不是string 就是Numeric
                    int num = (int) messageCell.getNumericCellValue();
                    message = String.valueOf(num);
                }*/

                DouyinOrder order = new DouyinOrder(orderNumber, orderNumber, goodsName, goodsId,
                         submitTime, finishTime, status, totalAmount, freight, orderType,
                        expressInformation, darenId, darenNickName, quantity, price);
                list.add(order);

            }

            long start = System.currentTimeMillis();

            try {
                result = orderService.saveBatch(list);
            } catch (Exception e) {
                log.error("快手订单 {} 导入出现异常 {}", fileName, e.getMessage());
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("快手订单 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("快手订单 {} 导入失败", fileName);
            }

        }
        list = null;//释放list
        System.gc();
        return result;
    }


    /**
     * 抖音资金账单
     * @param file
     * @return
     * @throws IOException
     */
    @ResponseBody
    @PostMapping("fund-bill")
    public boolean fundBill(MultipartFile file) throws IOException {
        boolean result = false;
        String fileName = file.getOriginalFilename();

        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        InputStreamReader is = new InputStreamReader(file.getInputStream());
        BufferedReader br = new BufferedReader(is);

        Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(br);
        List<CSVRecord> recordList = ((CSVParser) records).getRecords();
        int endLine = recordList.size();
        List<FundBill> list = new ArrayList<>();

        for(int i = 1; i < endLine; i++) {
            CSVRecord record = recordList.get(i);
            String createdTime = record.get(0).trim();
            String serialNumber = record.get(1).trim();
            String direction = record.get(2).trim();
            double orderAmount = Double.valueOf(record.get(3).trim());
            String account = record.get(4).trim();
            String summary = record.get(5).trim();
            String billType = record.get(6).trim();
            String subOrderNumber = record.get(7).trim();
            if(subOrderNumber.length() > 1) {
                subOrderNumber = subOrderNumber.substring(1);
            }
            String orderNumber = record.get(8).trim();
            if(orderNumber.length() > 1) {
                orderNumber = orderNumber.substring(1);
            }
            String afterSalesNumber = record.get(9).trim();
            if(afterSalesNumber.length() > 1) {
                afterSalesNumber = afterSalesNumber.substring(1);
            }
            String orderTime = record.get(10).trim();

            String goodsId = record.get(11).trim();
            if(goodsId.length() > 1) {
                goodsId = goodsId.substring(1);
            }
            String orderType = record.get(12).trim();
            double totalAmount = Double.valueOf(record.get(13).trim());
            double actuallyFreight = Double.valueOf(record.get(14).trim());
            double freightSubsidy = Double.valueOf(record.get(15).trim());
            double platformSubsidy = Double.valueOf(record.get(16).trim());
            double darenSubsidy = Double.valueOf(record.get(17).trim());
            double douyinSubsidy = Double.valueOf(record.get(18).trim());
            double marketingSubsidy = Double.valueOf(record.get(19).trim());
            double refundAmount = Double.valueOf(record.get(20).trim());
            double serviceCharge = Double.valueOf(record.get(21).trim());
            double commission = Double.valueOf(record.get(22).trim());
            double channelCommission = Double.valueOf(record.get(23).trim());
            double investmentServiceFee = Double.valueOf(record.get(24).trim());
            double promotionFee = Double.valueOf(record.get(25).trim());
            double otherCommission = Double.valueOf(record.get(26).trim());
            String remark = record.get(27).trim();


            FundBill fundBill = new FundBill( createdTime, serialNumber, direction, orderAmount,
                    account, summary, billType, subOrderNumber, orderNumber,
                    afterSalesNumber, orderTime, goodsId, orderType,
                    totalAmount, actuallyFreight, freightSubsidy, platformSubsidy,
                    darenSubsidy, douyinSubsidy, marketingSubsidy, refundAmount,
                    serviceCharge, commission, channelCommission, investmentServiceFee,
                    promotionFee, otherCommission, remark);
            list.add(fundBill);
        }

        if(list.size() > 0) {
            long start = System.currentTimeMillis();

            try {
                result = fundBillService.saveBatch(list);
            } catch (Exception e) {
                log.error("资金账单 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("资金账单 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("资金账单 {} 导入失败", fileName);
            }
        } else {
            log.warn("账单没有数据！");
        }

        list = null;//释放list
        System.gc();
        return result;
    }


    @ResponseBody
    @PostMapping("kuaishou-bill")
    public boolean kuaishouBill(@RequestParam MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        InputStream is = new FileInputStream(convFile);
        Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
                .bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
                .open(is);            // InputStream or File for XLSX file (required)
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<FundBill> list = new ArrayList<>();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println(sheet.getSheetName());
            DataFormatter df = new DataFormatter();
            int i = 0;//行数
            for (Row row : sheet) {
                if (i == 0) {//第1行跳过
                    i++;
                    continue;
                }
                if (row == null) {
                    break;
                }

                String orderNumber = row.getCell(1).getStringCellValue().trim();
                String goodsId = row.getCell(2).getStringCellValue().trim();
                String createdTime = row.getCell(5).getStringCellValue().trim();
                double totalAmount = row.getCell(9).getNumericCellValue();
                double refundAmount = row.getCell(10).getNumericCellValue();
                double commission = row.getCell(13).getNumericCellValue();
                String orderTime = row.getCell(24).getStringCellValue().trim();

                FundBill fundBill = new FundBill(createdTime, orderNumber, orderNumber, orderTime,
                        goodsId, totalAmount, refundAmount, commission);
                list.add(fundBill);

            }

            long start = System.currentTimeMillis();

            try {
                result = fundBillService.saveBatch(list);
            } catch (Exception e) {
                log.error("快手资金单 {} 导入出现异常 {}", fileName, e.getMessage());
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("快手资金单 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("快手资金单 {} 导入失败", fileName);
            }

        }
        list = null;//释放list
        System.gc();
        return result;
    }


    @ResponseBody
    @PostMapping("/douyin-quotation")
    public boolean douyinQuotation(@RequestParam MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<DouyinQuotation> list = new ArrayList<>();
        DataFormatter df = new DataFormatter();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 1;
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();

            //循环除了所有行,如果要循环除第一行以外的就firstRowNum
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                String goodsId = df.formatCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String goodsCategory = df.formatCellValue(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String adjustment = df.formatCellValue(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String manufacturer = df.formatCellValue(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String itemNumber = df.formatCellValue(row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String code = df.formatCellValue(row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String description = df.formatCellValue(row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String cost = df.formatCellValue(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String accessory = df.formatCellValue(row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String operatingFee = df.formatCellValue(row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String freight = df.formatCellValue(row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));

                DouyinQuotation quotation = new DouyinQuotation(goodsId, goodsCategory, adjustment, manufacturer, itemNumber,
                        code, description, cost, accessory, operatingFee, freight);
                list.add(quotation);

            }
            long start = System.currentTimeMillis();

            try {
                result = douyinQuotationService.saveBatch(list);
            } catch (Exception e) {
                log.error("抖音报价表 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("抖音报价表 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("抖音报价表 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }

    @ResponseBody
    @PostMapping("/live-quotation")
    public boolean liveQuotation(@RequestParam MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<LiveQuotation> list = new ArrayList<>();
        DataFormatter df = new DataFormatter();
        boolean result = false;
        if (workbook != null) {

            //获得当前sheet工作表
            Sheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的开始行
            int firstRowNum = sheet.getFirstRowNum() + 1;
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();

            //循环除了所有行,如果要循环除第一行以外的就firstRowNum
            for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                //获得当前行
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                String shop = df.formatCellValue(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String goodsId = df.formatCellValue(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String price = df.formatCellValue(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String totalCommission = df.formatCellValue(row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String onlineCommission = df.formatCellValue(row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String offlineCommission = df.formatCellValue(row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String status = df.formatCellValue(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));

                LiveQuotation quotation = new LiveQuotation(shop, goodsId, price, totalCommission,
                        onlineCommission, offlineCommission, status);
                list.add(quotation);

            }
            long start = System.currentTimeMillis();

            try {
                result = liveQuotationService.saveBatch(list);
            } catch (Exception e) {
                log.error("达人直播报价表 {} 导入出现异常 {}", fileName, e.getMessage());
                //e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("达人直播报价表 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("达人直播报价表 {} 导入失败", fileName);
            }
        }
        list = null;//释放list
        System.gc();
        return result;
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(HttpServletResponse response) {
        try {
            String columnNames[] = { "主订单编号", "子订单编号", "支付方式", "选购商品", "商品ID", "商品数量", "商品金额", "订单提交时间", "订单完成时间",
                    "买家留言", "旗帜颜色", "商家备注", "app渠道", "订单状态", "取消原因", "售后状态", "订单类型", "应付金额", "运费", "优惠总金额", "平台优惠",
                    "商家优惠", "达人优惠", "商家改价", "支付优惠", "手续费", "红包抵扣", "快递信息", "达人ID", "达人昵称", "广告渠道",
                    "动账时间", "下单时间", "贷款", "佣金"
            };// 列名
            String fields[] = { "orderNumber", "subOrderNumber", "paymentMethod", "goodsName", "goodsId",
                    "quantity", "price", "submitTime", "finishTime", "message", "color", "remark", "appChannel",
                    "status", "cancelReason", "afterSalesStatus", "orderType", "totalAmount", "freight",
                    "totalDiscount", "platformOffers", "merchantOffers", "darenOffers", "changePrice",
                    "paymentDiscount", "fee", "redEnvelope", "expressInformation", "darenId", "darenNickName", "adChannel",
                    "createdTime", "orderTime", "loan", "commission"

            };// 字段名
//            Map<String, String> myMap = new LinkedHashMap<>();
//            myMap
            File file = new File("daren.xlsx");
            file.createNewFile(); // if file already exists will do nothing

//            FileOutputStream oFile = new FileOutputStream(file, false);
//            oFile.close();

            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook wb_template = new XSSFWorkbook(inputStream);
            inputStream.close();

            SXSSFWorkbook wb = new SXSSFWorkbook(wb_template);
            wb.setZip64Mode(Zip64Mode.Always);
            wb.setCompressTempFiles(true);
            wb.removeSheetAt(0);
            SXSSFSheet sheet = wb.createSheet();
            //SXSSFSheet sheet = (SXSSFSheet) wb.getSheetAt(0);
            sheet.setRandomAccessWindowSize(1000);// keep 100 rows in memory, exceeding rows will be flushed to disk
            Row headRow = sheet.createRow(0);
            for (int i = 0; i < columnNames.length; i++) {
                headRow.createCell(i).setCellValue(columnNames[i]);
            }
            List<DaRenVO> daRens = orderService.listAll();
            for (int i = 0; i < daRens.size(); i++) {
                DaRenVO daRen = daRens.get(i);
                Row row = sheet.createRow(i + 1);

                for (int j = 0; j < fields.length; j++) {
                    row.createCell(j).setCellValue(getGetMethod(daRen, fields[j]));
                }
                /*
                row.createCell(0).setCellValue(getGetMethod(daRen, "subOrderNumber"));
                row.createCell(1).setCellValue(daRen.getPaymentMethod());
                row.createCell(2).setCellValue(daRen.getGoodsName());
                row.createCell(3).setCellValue(daRen.getGoodsId());
                row.createCell(4).setCellValue(daRen.getQuantity());
                row.createCell(5).setCellValue(daRen.getPrice());
                row.createCell(6).setCellValue(daRen.getSubmitTime());
                row.createCell(7).setCellValue(daRen.getFinishTime());
                row.createCell(8).setCellValue(daRen.getMessage());
                row.createCell(9).setCellValue(daRen.getColor());
                row.createCell(10).setCellValue(daRen.getRemark());
                row.createCell(11).setCellValue(daRen.getStatus());
                row.createCell(12).setCellValue(daRen.getCancelReason());
                row.createCell(13).setCellValue(daRen.getAfterSalesStatus());
                row.createCell(14).setCellValue(daRen.getTotalAmount());
                row.createCell(15).setCellValue(daRen.getFreight());
                row.createCell(16).setCellValue(daRen.getTotalDiscount());
                row.createCell(17).setCellValue(daRen.getPlatformOffers());
                row.createCell(18).setCellValue(daRen.getMerchantOffers());
                row.createCell(19).setCellValue(daRen.getDarenOffers());
                row.createCell(20).setCellValue(daRen.getChangePrice());
                row.createCell(21).setCellValue(daRen.getPaymentDiscount());
                row.createCell(22).setCellValue(daRen.getFee());
                row.createCell(23).setCellValue(daRen.getRedEnvelope());
                row.createCell(24).setCellValue(daRen.getExpressInformation());
                row.createCell(25).setCellValue(daRen.getDarenId());
                row.createCell(26).setCellValue(daRen.getDarenNickName());
                row.createCell(27).setCellValue(daRen.getCreatedTime());
                row.createCell(28).setCellValue(daRen.getOrderTime());
                row.createCell(29).setCellValue(daRen.getLoan());
                row.createCell(30).setCellValue(daRen.getCommission());*/
            }

            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=daren.xlsx");

            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String getGetMethod(DaRenVO daRen, String fieldName) {
        Class clazz = daRen.getClass();
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            Method method = clazz.getDeclaredMethod(sb.toString());
            Object value = method.invoke(daRen);
            if (value instanceof Number) {
                return value.toString();
            }
            return (String) method.invoke(daRen);
        } catch (Exception e) {
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        DaRenVO order = new DaRenVO();
        order.setLoan(112.1);

        System.out.println(getGetMethod(order, "loan"));
//        Class clazz = order.getClass();
//        Method m1 = clazz.getDeclaredMethod("getSubOrderNumber");
//        m1.invoke(order);
//        String msg = (String) m1.invoke(order);
//        System.out.println(msg);
    }

}
