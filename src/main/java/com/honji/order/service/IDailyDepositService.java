package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DepositVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
public interface IDailyDepositService extends IService<DailyDeposit> {

    PageInfo<DepositVo> listByCurrentUser(int offset, int limit);
}
