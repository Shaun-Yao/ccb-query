package com.honji.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.honji.order.entity.Authority;
import com.honji.order.entity.Bank;
import com.honji.order.entity.CashBalance;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.IAuthorityService;
import com.honji.order.service.IBankService;
import com.honji.order.service.ICashBalanceService;
import com.honji.order.service.IDailyDepositService;
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
    public DataGridResult query(@RequestParam String jobNum, @RequestParam(value = "shopCodes[]", required = false) List<String> shopCodes,
                                @RequestParam(defaultValue = "0") int offset,
                                @RequestParam int limit ) {
        return new DataGridResult(dailyDepositService.listByShopCodes(jobNum, shopCodes, offset, limit));
    }


    @GetMapping("/get")
    @ResponseBody
    public DailyDeposit get(@RequestParam String id) {

        return dailyDepositService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam String shopCode, @RequestParam(defaultValue = "0") int offset, @RequestParam int limit) {
        //Admin admin = (Admin) session.getAttribute("admin");
//        PageHelper.startPage(offset / limit + 1, limit);
//        QueryWrapper<DailyDeposit> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("khdm", "Z75320");
//        List<DailyDeposit> deposits = dailyDepositService.list(queryWrapper);
//        PageInfo<DailyDeposit> depositPageInfo = new PageInfo<>(deposits);
        //new DepositVo(1l, );
        return new DataGridResult(dailyDepositService.listByCurrentUser(shopCode, offset, limit));
//        IPage<DailyDeposit> dailyDepositPage = new Page<>(offset / limit + 1, limit);
//        return new DataGridResult(dailyDepositService.list(dailyDepositPage, "Z75320"));

    }

    @PostMapping("/save")
    @ResponseBody
    public boolean save(@ModelAttribute DailyDeposit dailyDeposit) {
        return dailyDepositService.saveOrUpdate(dailyDeposit);
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
