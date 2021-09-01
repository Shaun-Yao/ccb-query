package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Deposit;
import com.honji.order.entity.Sale;

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
}
