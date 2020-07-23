package com.honji.order.service;

import com.honji.order.entity.CashBalance;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
