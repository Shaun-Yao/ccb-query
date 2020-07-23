package com.honji.order.service.impl;

import com.honji.order.entity.CashBalance;
import com.honji.order.mapper.CashBalanceMapper;
import com.honji.order.service.ICashBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
@Service
public class CashBalanceServiceImpl extends ServiceImpl<CashBalanceMapper, CashBalance> implements ICashBalanceService {

    @Autowired
    private CashBalanceMapper cashBalanceMapper;

    @Override
    public double calBalance(String shopCode) {
        return cashBalanceMapper.calBalance(shopCode);
    }
}
