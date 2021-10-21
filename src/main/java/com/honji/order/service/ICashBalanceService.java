package com.honji.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.CashBalance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.order.model.BalanceDTO;
import com.honji.order.model.BalanceVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
public interface ICashBalanceService extends IService<CashBalance> {

    double calBalance(String shopCode);

    PageInfo<CashBalance> listByShopCode(String shopCode, int offset, int limit);

    PageInfo<BalanceVO> query(BalanceDTO balanceDTO);

    List<BalanceVO> listAll(BalanceDTO dto);
}
