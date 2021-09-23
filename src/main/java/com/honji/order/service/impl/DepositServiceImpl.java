package com.honji.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Deposit;
import com.honji.order.mapper.DepositMapper;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
import com.honji.order.service.IDepositService;
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

    @Override
    public PageInfo<DepositVO> search(DepositDTO depositDTO) {

        PageHelper.startPage(depositDTO.getOffset() / depositDTO.getLimit() + 1, depositDTO.getLimit());
        List<DepositVO> depositVos = new ArrayList<>();
        if (depositDTO.getShopCodes() != null && depositDTO.getShopCodes().size() > 0) {//店铺集合不为空才查询
            depositVos = depositMapper.search(depositDTO);
        }
        return new PageInfo<>(depositVos);
    }

    @Override
    public List<DepositVO> listAll(DepositDTO depositDTO) {
        List<DepositVO> depositVos = depositMapper.search(depositDTO);
        return depositVos;
    }

}
