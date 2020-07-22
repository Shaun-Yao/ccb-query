package com.honji.order.controller;


import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.IDailyDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
@Controller
@RequestMapping("/daily-deposit")
public class DailyDepositController {

    @Autowired
    private IDailyDepositService dailyDepositService;

    @GetMapping("/index")
    public String index(@RequestParam String shopCode) {
        System.out.println(shopCode);
        return "daily_deposit";
    }

    @GetMapping("/get")
    public String get(@RequestParam Long id, Model model) {

        //DailyDeposit dailyDeposit = dailyDepositService.getById(id);
        List<Long> list = Arrays.asList(1l,2l,3l, 4l, 5l);
        List<DailyDeposit> deposits = dailyDepositService.listByIds(list);
        model.addAttribute("deposits", deposits);
        return "daily_deposit";
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
        return new DataGridResult(dailyDepositService.list(offset, limit));
//        IPage<DailyDeposit> dailyDepositPage = new Page<>(offset / limit + 1, limit);
//        return new DataGridResult(dailyDepositService.list(dailyDepositPage, "Z75320"));

    }


    @GetMapping("/showImage")
    @ResponseBody
    public byte[] showImage(@RequestParam Long id) {

        DailyDeposit dailyDeposit = dailyDepositService.getById(id);

        return dailyDeposit.getPicture();
    }

}
