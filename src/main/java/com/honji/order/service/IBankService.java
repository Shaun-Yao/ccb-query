package com.honji.order.service;

import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Bank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.order.entity.CashBalance;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
public interface IBankService extends IService<Bank> {

    PageInfo<Bank> listByAccount(String account, int offset, int limit);
}
