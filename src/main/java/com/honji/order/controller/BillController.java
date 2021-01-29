package com.honji.order.controller;


import com.honji.order.model.BillDTO;
import com.honji.order.model.DataGridResult;
import com.honji.order.service.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yao
 * @since 2021-01-05
 */
@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private IBillService billService;

    @GetMapping("/index")
    public String index(Model model) {

        return "bill";
    }

    @ResponseBody
    @GetMapping("/list")
    public DataGridResult list(BillDTO billDTO) {
        System.out.println("=======" + billDTO.getBillTypes());
        return new DataGridResult(billService.listForIndex(billDTO));
    }

    @PostMapping("/remove")
    @ResponseBody
    public boolean remove(BillDTO billDTO) {
        System.out.println("=======" + billDTO.getBillTypes());
//        String[] yearMonth = month.split("-");
//        LocalDateTime begin = LocalDateTime.of(Integer.valueOf(yearMonth[0]),
//                Integer.valueOf(yearMonth[1]), 1, 0,0, 0);
//        LocalDateTime end = LocalDateTime.of(Integer.valueOf(yearMonth[0]),
//                Integer.valueOf(yearMonth[1]), 31, 23,59, 59);
//        QueryWrapper<Bill> billQueryWrapper = new QueryWrapper<>();
//        billQueryWrapper.in("type", types);
//        billService.remove(billQueryWrapper);
        billService.removeByMonth(billDTO);
        return true;
    }

}
