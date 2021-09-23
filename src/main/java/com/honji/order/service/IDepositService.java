package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Deposit;
import com.honji.order.entity.Sale;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2021-08-31
 */
public interface IDepositService extends IService<Deposit> {

    PageInfo<Deposit> listForIndex(String shopCode, int offset, int limit);

    PageInfo<DepositVO> search(DepositDTO depositDTO);

    List<DepositVO> listAll(DepositDTO depositDTO);
}
