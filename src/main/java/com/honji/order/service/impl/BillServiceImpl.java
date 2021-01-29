package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Bill;
import com.honji.order.mapper.BillMapper;
import com.honji.order.model.BillDTO;
import com.honji.order.model.DepositVO;
import com.honji.order.service.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements IBillService {

    @Autowired
    private BillMapper billMapper;

    @Override
    public PageInfo<Bill> listForIndex(BillDTO billDTO) {
        PageHelper.startPage(billDTO.getOffset() / billDTO.getLimit() + 1, billDTO.getLimit());
        List<Bill> bills = billMapper.selectForIndex(billDTO);
        return new PageInfo<>(bills);
    }

    @Override
    public void removeByMonth(BillDTO billDTO) {
        billMapper.deleteByMonth(billDTO);
    }
}
