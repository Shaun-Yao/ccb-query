package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Deposit;
import com.honji.order.mapper.DepositMapper;
import com.honji.order.service.IDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2021-08-31
 */
@Service
public class DepositServiceImpl extends ServiceImpl<DepositMapper, Deposit> implements IDepositService {

    @Autowired
    private DepositMapper depositMapper;

    @Override
    public PageInfo<Deposit> listForIndex(String shopCode, int offset, int limit) {
        PageHelper.startPage(offset / limit + 1, limit);
        return new PageInfo<>(depositMapper.selectForIndex(shopCode));
    }


}
