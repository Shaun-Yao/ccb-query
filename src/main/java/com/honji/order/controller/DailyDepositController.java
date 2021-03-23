package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.honji.order.entity.Authority;
import com.honji.order.entity.Bank;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
import com.honji.order.service.IAuthorityService;
import com.honji.order.service.IBankService;
import com.honji.order.service.ICashBalanceService;
import com.honji.order.service.IDailyDepositService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
@Slf4j
@Controller
@RequestMapping("/daily-deposit")
public class DailyDepositController {

    @Value("${web.upload-path}")
    private String uploadPath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private IDailyDepositService dailyDepositService;

    @Autowired
    private IBankService bankService;

    @Autowired
    private IAuthorityService authorityService;

    @Autowired
    private ICashBalanceService cashBalanceService;

    @GetMapping("/index")
    public String index(@RequestParam String shopCode, Model model) {
/*

        String user = (String) session.getAttribute("user");
        if (StringUtils.isEmpty(user)) {
            System.out.println(222);
            session.setAttribute("user", shopCode);
        }
*/

        List<Bank> banks = null;
        QueryWrapper<CashBalance> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper.eq("khdm", shopCode);
        CashBalance cashBalance = cashBalanceService.getOne(shopQueryWrapper);
        if (cashBalance.getType() == 1) {
            QueryWrapper<Bank> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("type", "account");
            banks = bankService.list(queryWrapper);
        } else {
            QueryWrapper<Bank> queryWrapper = new QueryWrapper<>();
            queryWrapper.ne("type", "1");
            banks = bankService.list(queryWrapper);
        }

        model.addAttribute("shopCode", shopCode);
        model.addAttribute("banks", banks);
        return "daily_deposit";
    }

    @GetMapping("/toQuery")
    public String query(@RequestParam String jobNum, Model model) {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_num", jobNum);
//        StringBuffer shopCodes = new StringBuffer();
        List<Authority> authorities = authorityService.list(queryWrapper);
        model.addAttribute("authorities", authorities);
        model.addAttribute("jobNum", jobNum);
        return "query";
    }

    @GetMapping("/query")
    @ResponseBody
    public DataGridResult query(DepositDTO depositDTO) {
        return new DataGridResult(dailyDepositService.listByShopCodes(depositDTO));
    }

    @GetMapping("/export")
    @ResponseBody
    public void export(DepositDTO depositDTO, HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        String columnNames[] = { "店铺代码", "店铺名称", "营业日期", "营业额", "结余", "现金调整",
                "POS机/刷卡/银联扫码", "广发兑换券", "建行扫码", "建行离线", "支付宝", "微信", "扫一扫", "码上收",
                "百胜支付", "商场代收款", "合胜收款", "现金", "会员积分/储值卡消费/礼券",
                "多收款", "悦支付","存款银行", "存款日期", "存款额"};// 列名
        //erp版本
//        String columnNames[] = { "店铺代码", "店铺名称", "营业日期", "营业额", "结余", "现金调整",
//                "刷卡", "广发兑换券", "建行扫码", "建行离线", "支付宝", "微信", "扫一扫", "码上收",
//                "百胜支付", "商场代收款", "合胜收款", "现金", "会员积分", "储值卡消费", "礼券",
//                "多收款", "悦支付","存款银行", "存款日期", "存款额"};// 列名
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        List<DepositVO> deposits = dailyDepositService.listAll(depositDTO);
        for (int i = 0; i < deposits.size(); i++) {
            DepositVO deposit = deposits.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(deposit.getKhdm());
            row.createCell(1).setCellValue(deposit.getKhmc());
            Cell dateCell = row.createCell(2);
            dateCell.setCellValue(deposit.getDate());
            dateCell.setCellStyle(cellStyle);
            row.createCell(3).setCellValue(deposit.getAmount());
            row.createCell(4).setCellValue(deposit.getBalance());
            row.createCell(5).setCellValue(deposit.getCashAdjustment());
            row.createCell(6).setCellValue(deposit.getPos());
            row.createCell(7).setCellValue(deposit.getCgbCoupon());
            row.createCell(8).setCellValue(deposit.getCcbZs());
            row.createCell(9).setCellValue(deposit.getCcbBs());
            row.createCell(10).setCellValue(deposit.getAlipay());
            row.createCell(11).setCellValue(deposit.getWxpay());
            row.createCell(12).setCellValue(deposit.getSys());
            row.createCell(13).setCellValue(deposit.getMss());
            row.createCell(14).setCellValue(deposit.getBsPay());
            row.createCell(15).setCellValue(deposit.getMallCollection());
            row.createCell(16).setCellValue(deposit.getHeSheng());
            row.createCell(17).setCellValue(deposit.getCash());
            row.createCell(18).setCellValue(deposit.getMemberPoints());
            row.createCell(19).setCellValue(deposit.getExtraCash());
            row.createCell(20).setCellValue(deposit.getYuePay());
            row.createCell(21).setCellValue(deposit.getBankName());
            Cell depositDateCell = row.createCell(22);
            depositDateCell.setCellValue(deposit.getDepositDate());
            depositDateCell.setCellStyle(cellStyle);
            row.createCell(23).setCellValue(deposit.getDeposit());

            /*erp版本
            row.createCell(3).setCellValue(deposit.getAmount());
            row.createCell(4).setCellValue(deposit.getBalance());
            row.createCell(5).setCellValue(deposit.getCashAdjustment());
            row.createCell(6).setCellValue(deposit.getCardPay());
            row.createCell(7).setCellValue(deposit.getCgbCoupon());
            row.createCell(8).setCellValue(deposit.getCcbZs());
            row.createCell(9).setCellValue(deposit.getCcbBs());
            row.createCell(10).setCellValue(deposit.getAlipay());
            row.createCell(11).setCellValue(deposit.getWxpay());
            row.createCell(12).setCellValue(deposit.getSys());
            row.createCell(13).setCellValue(deposit.getMss());
            row.createCell(14).setCellValue(deposit.getBsPay());
            row.createCell(15).setCellValue(deposit.getMallCollection());
            row.createCell(16).setCellValue(deposit.getHeSheng());
            row.createCell(17).setCellValue(deposit.getCash());
            row.createCell(18).setCellValue(deposit.getMemberPoints());
            row.createCell(19).setCellValue(deposit.getCardConsumption());
            row.createCell(20).setCellValue(deposit.getGiftCertificate());
            row.createCell(21).setCellValue(deposit.getExtraCash());
            row.createCell(22).setCellValue(deposit.getYuePay());
            row.createCell(23).setCellValue(deposit.getBankName());
            Cell depositDateCell = row.createCell(24);
            depositDateCell.setCellValue(deposit.getDepositDate());
            depositDateCell.setCellStyle(cellStyle);
            row.createCell(25).setCellValue(deposit.getDeposit());
            */
        }

        try {
            response.reset();
//            response.setContentType("application/vnd.ms-excel");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("存款数据.xlsx", "utf-8"));
            OutputStream out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/get")
    @ResponseBody
    public DailyDeposit get(@RequestParam String id) {

        return dailyDepositService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam String shopCode,
                               @RequestParam(defaultValue = "0") int offset, @RequestParam int limit) {
        //Admin admin = (Admin) session.getAttribute("admin");
//        PageHelper.startPage(offset / limit + 1, limit);
//        QueryWrapper<DailyDeposit> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("khdm", "Z75320");
//        List<DailyDeposit> deposits = dailyDepositService.list(queryWrapper);
//        PageInfo<DailyDeposit> depositPageInfo = new PageInfo<>(deposits);
        //new DepositVO(1l, );
        return new DataGridResult(dailyDepositService.listByCurrentUser(shopCode, offset, limit));
//        IPage<DailyDeposit> dailyDepositPage = new Page<>(offset / limit + 1, limit);
//        return new DataGridResult(dailyDepositService.list(dailyDepositPage, "Z75320"));

    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute DailyDeposit dailyDeposit) {
        return dailyDepositService.saveOrUpdate(dailyDeposit);
    }

    @PostMapping("/updateCashAdjustment")
    @ResponseBody
    public boolean updateCashAdjustment(@ModelAttribute DailyDeposit dailyDeposit) {
        UpdateWrapper<DailyDeposit> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", dailyDeposit.getId()).set("cash_adjustment", dailyDeposit.getCashAdjustment());
//        updateWrapper.eq("cash_adjustment", dailyDeposit.getCashAdjustment());
        return dailyDepositService.update(updateWrapper);
    }

    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam String id) {
        return dailyDepositService.removeById(id);
    }

    @ResponseBody
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam String shopCode) {
//        String user = (String) session.getAttribute("user");
        //文件名由用户id-时间组成，如150-20200408095444759.png
        //TODO current user
        StringBuffer newFileName = new StringBuffer(shopCode).append("-");
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();//原文件名
                byte[] bytes = file.getBytes();
                int idx = fileName.lastIndexOf(".");
                String suffix= fileName.substring(idx); //文件后缀
                String time = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS");
                //newFileName = time.concat(suffix);
                String filePath = uploadPath.concat(newFileName.append(time).append(suffix).toString());
                Path path = Paths.get(filePath);
                Files.write(path, bytes);

            } catch (IOException e) {
                log.error("上传图片失败");
                e.printStackTrace();
            }
        }
        return newFileName.toString();
    }

    @GetMapping("/showImage")
    @ResponseBody
    public ResponseEntity showImage(@RequestParam String fileName) {
        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + uploadPath.concat(fileName)));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
