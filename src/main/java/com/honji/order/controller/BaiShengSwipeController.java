package com.honji.order.controller;


import com.honji.order.entity.BaiShengSwipe;
import com.honji.order.service.IBaiShengSwipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2020-05-12
 */
@Slf4j
@Controller
@RequestMapping("/bai-sheng-swipe")
public class BaiShengSwipeController {

    @Autowired
    private IBaiShengSwipeService baiShengSwipeService;

    @ResponseBody
    @PostMapping("/import")
    public boolean importOrder(@RequestParam("swipeFile") MultipartFile file) throws IOException {

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
                String terminalNum = terminalCell.getStringCellValue();
                double amount = amountCell.getNumericCellValue();
                double fee = feeCell.getNumericCellValue();
                String merchant = merchantCell.getStringCellValue();
                String orderId = orderCell.getStringCellValue();

                BaiShengSwipe baiShengSwipe = new BaiShengSwipe(date, time, amount, fee, terminalNum, orderId, merchant);
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

}
