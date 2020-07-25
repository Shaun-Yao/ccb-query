package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.mapper.DailyDepositMapper;
import com.honji.order.model.DepositVo;
import com.honji.order.service.IDailyDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
@Service
public class DailyDepositServiceImpl extends ServiceImpl<DailyDepositMapper, DailyDeposit> implements IDailyDepositService {

    @Autowired
    private DailyDepositMapper dailyDepositMapper;

    @Autowired
    private HttpSession session;

    @Override
    public PageInfo<DepositVo> listByCurrentUser(String shopCode, int offset, int limit) {
//        String user = (String) session.getAttribute("user");
        PageHelper.startPage(offset / limit + 1, limit);
        List<DepositVo> depositVos = dailyDepositMapper.selectByShopCode(shopCode);
        //Page<DepositVo> depositVoPage = dailyDepositMapper.selectByShopCode("Z75320");
        return new PageInfo<>(depositVos);
    }
}
