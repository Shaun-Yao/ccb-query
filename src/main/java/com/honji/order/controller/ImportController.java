package com.honji.order.controller;


import com.honji.order.entity.BaiShengPay;
import com.honji.order.service.IBaiShengPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    @GetMapping("/index")
    public String index() {

        return "index";
    }

    @ResponseBody
    @PostMapping("/baiShengPay")
    public boolean baiShengPay(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();
        //checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
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
                LocalDate date = dateCell.getLocalDateTimeCellValue().toLocalDate();
                String time = timeCell.getStringCellValue();
                String terminalNum = terminalCell.getStringCellValue();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = merchantCell.getStringCellValue();
                String orderId = orderCell.getStringCellValue();

                BaiShengPay baiShengPay = new BaiShengPay(date, time, amount, fee, terminalNum, orderId, merchant);
                list.add(baiShengPay);

                //baiShengPayService.saveBatch(list);
                //timeCell.getStringCellValue()
                //System.out.println(cell.getLocalDateTimeCellValue().toLocalDate());
                //System.out.println(baiShengPay);

                /*
                //获得当前行的开始列
                int firstCellNum = row.getFirstCellNum();
                //获得当前行的列数
                int lastCellNum = row.getLastCellNum();
                if (lastCellNum > 0) {
                    ArrayList<Object> cellValues = new ArrayList<>();
                    //循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cellValues.add(getCellValue(cell));
                        //System.out.println(getCellValue(cell));
                    }
                    //list.add(cellValues);
                }*/
            }
            long start = System.currentTimeMillis();

            try {
                result = baiShengPayService.saveBatch(list);
            } catch (Exception e) {
                log.error("百胜支付 {} 导入出现异常", fileName);
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            if (result) {
                log.info("百胜支付 {} 导入成功，耗时{}秒", fileName, (start - end) / 1000);
            } else {
                log.error("百胜支付 {} 导入失败", fileName);
            }

        }
        return result;
    }

    /**
     * 检查文件
     *
     * @param file
     * @throws IOException
     */
    private void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            System.err.println("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            System.err.println("不是excel文件");
        }
    }

    public Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            workbook = WorkbookFactory.create(is);
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            /*if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007 及2007以上
                workbook = WorkbookFactory.create(is);
            }*/
        } catch (IOException e) {
            //e.getMessage();
            e.printStackTrace();
        }
        return workbook;
    }


    public String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //判断数据的类型
        //判断数据的类型
        switch (cell.getCellType()) {
            case NUMERIC: //数字
                cellValue = stringDateProcess(cell);
                break;
            case STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: //空值
                cellValue = "";
                break;
            case ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }



    public static String stringDateProcess(Cell cell) {
        String result = new String();
        if (DateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                sdf = new SimpleDateFormat("HH:mm");
            } else {// 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            }
            Date date = cell.getDateCellValue();
            result = sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 58) {
            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            double value = cell.getNumericCellValue();
            Date date = org.apache.poi.ss.usermodel.DateUtil
                    .getJavaDate(value);
            result = sdf.format(date);
        } else {
            double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if (temp.equals("General")) {
                format.applyPattern("#");
            }
            result = format.format(value);
        }

        return result;
    }
/*
    public static InputStream convertorStream(Workbook workbook) {
        InputStream in = null;
        try {
            //临时缓冲区
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //创建临时文件
            workbook.write(out);
            byte[] bookByteAry = out.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }
    */
}
