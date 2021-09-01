package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Bill;
import com.honji.order.entity.Sale;
import com.honji.order.mapper.BillMapper;
import com.honji.order.mapper.SaleMapper;
import com.honji.order.model.BillDTO;
import com.honji.order.service.IBillService;
import com.honji.order.service.ISaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2021-01-05
 */
@Service
public class SaleServiceImpl extends ServiceImpl<SaleMapper, Sale> implements ISaleService {

    @Autowired
    private SaleMapper saleMapper;

    @Override
    public PageInfo<Sale> listForIndex(String shopCode, int offset, int limit) {
        PageHelper.startPage(offset / limit + 1, limit);
        return new PageInfo<>(saleMapper.selectForIndex(shopCode));
    }


}
