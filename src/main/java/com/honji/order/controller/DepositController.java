package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.Authority;
import com.honji.order.entity.Bank;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.Deposit;
import com.honji.order.enums.ShopTypeEnum;
import com.honji.order.model.DataGridResult;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
import com.honji.order.service.IAuthorityService;
import com.honji.order.service.IBankService;
import com.honji.order.service.ICashBalanceService;
import com.honji.order.service.IDepositService;
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
 * @since 2020-08-30
 */
@Slf4j
@Controller
@RequestMapping("/deposit")
public class DepositController {

    @Value("${web.deposit-path}")
    private String uploadPath;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private IDepositService depositService;

    @Autowired
    private ICashBalanceService cashBalanceService;

    @Autowired
    private IBankService bankService;

    @Autowired
    private IAuthorityService authorityService;


    @GetMapping("/index")
    public String index(@RequestParam String shopCode, Model model) {

        List<Bank> banks = null;
        QueryWrapper<CashBalance> shopQueryWrapper = new QueryWrapper<>();
        shopQueryWrapper.eq("khdm", shopCode);
        CashBalance cashBalance = cashBalanceService.getOne(shopQueryWrapper);
        QueryWrapper<Bank> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enabled", 1);
        if (ShopTypeEnum.PUBLIC.equals(cashBalance.getType())) {//公户门店可以存到所有分店账号

            queryWrapper.orderByAsc("type", "account");
            banks = bankService.list(queryWrapper);
        } else {//私户门店没有分店账号
            queryWrapper.ne("type", "1");
            banks = bankService.list(queryWrapper);
        }

        model.addAttribute("shopCode", shopCode);
        model.addAttribute("banks", banks);
        return "deposit";
    }

    @GetMapping("/to-query")
    public String query(@RequestParam String jobNum, Model model) {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_num", jobNum);
        List<Authority> authorities = authorityService.list(queryWrapper);
        model.addAttribute("authorities", authorities);
        model.addAttribute("jobNum", jobNum);
        return "query";
    }


    @GetMapping("/query")
    @ResponseBody
    public DataGridResult query(DepositDTO depositDTO) {
        return new DataGridResult(depositService.search(depositDTO));
    }


    @GetMapping("/get")
    @ResponseBody
    public Deposit get(@RequestParam String id) {

        return depositService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam String shopCode, @RequestParam int offset, @RequestParam int limit) {
        return new DataGridResult(depositService.listForIndex(shopCode, offset, limit));
    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute Deposit deposit) {
        return depositService.saveOrUpdate(deposit);
    }



    @GetMapping("/export")
    @ResponseBody
    public void export(DepositDTO depositDTO, HttpServletResponse response) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");

        String columnNames[] = { "店铺代码", "店铺名称", "营业日期", "营业额", "结余",
                "刷卡", "广发兑换券", "建行扫码", "建行离线", "支付宝", "微信", "扫一扫", "码上收",
                "百胜支付", "商场代收款", "合胜收款", "现金", "会员积分", "储值卡消费", "礼券",
                "悦支付", "万达支付", "存款银行", "存款日期", "存款额"};// 列名
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        Row headRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
            headRow.createCell(i).setCellValue(columnNames[i]);
        }
        List<DepositVO> deposits = depositService.listAll(depositDTO);
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
            row.createCell(5).setCellValue(deposit.getCardPay());
            row.createCell(6).setCellValue(deposit.getCgbCoupon());
            row.createCell(7).setCellValue(deposit.getCcbZs());
            row.createCell(8).setCellValue(deposit.getCcbBs());
            row.createCell(9).setCellValue(deposit.getAlipay());
            row.createCell(10).setCellValue(deposit.getWxpay());
            row.createCell(11).setCellValue(deposit.getSys());
            row.createCell(12).setCellValue(deposit.getMss());
            row.createCell(13).setCellValue(deposit.getBsPay());
            row.createCell(14).setCellValue(deposit.getMallCollection());
            row.createCell(15).setCellValue(deposit.getHeSheng());
            row.createCell(16).setCellValue(deposit.getCash());
            row.createCell(17).setCellValue(deposit.getMemberPoints());
            row.createCell(18).setCellValue(deposit.getCardConsumption());
            row.createCell(19).setCellValue(deposit.getGiftCertificate());
            row.createCell(20).setCellValue(deposit.getYuePay());
            row.createCell(21).setCellValue(deposit.getWanDa());
            row.createCell(22).setCellValue(deposit.getBankName());
            Cell depositDateCell = row.createCell(23);
            depositDateCell.setCellValue(deposit.getDepositDate());
            depositDateCell.setCellStyle(cellStyle);
            row.createCell(24).setCellValue(deposit.getDeposit());
        }

        try {
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(
                    "营业和存款数据".concat(depositDTO.getBegin()).concat("至").concat(depositDTO.getEnd()).concat(".xlsx"), "utf-8"));
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
    public boolean remove(@RequestParam String id) {
        return depositService.removeById(id);
    }

    @ResponseBody
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam String shopCode) {
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
