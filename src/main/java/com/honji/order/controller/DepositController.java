package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.Bank;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.Deposit;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.IBankService;
import com.honji.order.service.ICashBalanceService;
import com.honji.order.service.IDepositService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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



    @GetMapping("/index")
    public String index(@RequestParam String shopCode, Model model) {

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
        return "deposit";
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
