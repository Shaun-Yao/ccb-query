package com.honji.order.mapper;

import com.github.pagehelper.PageHelper;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DailyDepositMapperTest {

    @Autowired
    private DailyDepositMapper depositMapper;

    @Test
    public void crossDBTest() {
        PageHelper.startPage(1, 10);
        DepositDTO depositDTO = new DepositDTO();
        List<String> shopCodes = Arrays.asList("Z59539", "Z02812");
        depositDTO.setShopCodes(shopCodes);

//        depositDTO.setDepositBegin("2020-12-01");
//        depositDTO.setDepositEnd("2020-12-31");
        depositDTO.setBegin("2020-12-01");
        depositDTO.setEnd("2020-12-31");
        depositMapper.selectByShopCodes(depositDTO);
//        depositMapper.test(depositDTO);
    }

    @Test
    public void testProc() {
//        PageHelper.startPage(1, 10);
        DepositDTO depositDTO = new DepositDTO();
        List<String> shopCodes = Arrays.asList("Z59539", "Z02812");
        depositDTO.setShopCodes(shopCodes);
        depositDTO.setDepositBegin("2020-12-01");
        depositDTO.setDepositEnd("2020-12-31");
        depositDTO.setBegin("2020-12-01");
        depositDTO.setEnd("2020-12-31");
        String shopCodeStr = depositDTO.getShopCodes().stream()
                .collect(Collectors.joining(","));
//        depositDTO.setShopCodeStr(shopCodeStr);
        System.out.println(shopCodeStr);
        List<DepositVO> depositVOs = depositMapper.testProc(depositDTO);
    }
}

