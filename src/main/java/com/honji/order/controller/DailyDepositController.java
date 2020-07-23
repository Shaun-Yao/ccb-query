package com.honji.order.controller;


import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.IDailyDepositService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

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

    private final String uploadPath = "\\\\10.10.10.188\\183财务存款票据\\";

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private IDailyDepositService dailyDepositService;

    @GetMapping("/index")
    public String index(@RequestParam String shopCode) {
        System.out.println(shopCode);
        return "daily_deposit";
    }

    @GetMapping("/get")
    @ResponseBody
    public DailyDeposit get(@RequestParam Long id) {

        return dailyDepositService.getById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public DataGridResult list(@RequestParam(defaultValue = "0") int offset, @RequestParam int limit) {
        //Admin admin = (Admin) session.getAttribute("admin");
//        PageHelper.startPage(offset / limit + 1, limit);
//        QueryWrapper<DailyDeposit> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("khdm", "Z75320");
//        List<DailyDeposit> deposits = dailyDepositService.list(queryWrapper);
//        PageInfo<DailyDeposit> depositPageInfo = new PageInfo<>(deposits);
        //new DepositVo(1l, );
        return new DataGridResult(dailyDepositService.listByCurrentUser(offset, limit));
//        IPage<DailyDeposit> dailyDepositPage = new Page<>(offset / limit + 1, limit);
//        return new DataGridResult(dailyDepositService.list(dailyDepositPage, "Z75320"));

    }

    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(@RequestParam Long id) {
        return dailyDepositService.removeById(id);
    }

    @ResponseBody
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        //UserSessionVO user = (UserSessionVO) session.getAttribute("user");
        //文件名由用户id-时间组成，如150-20200408095444759.png
        //TODO current user
        StringBuffer newFileName = new StringBuffer("Z001").append("-");
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
