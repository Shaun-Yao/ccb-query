package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DepositVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
public interface IDailyDepositService extends IService<DailyDeposit> {

    PageInfo<DepositVo> listByCurrentUser(String shopCode, int offset, int limit);
    PageInfo<DepositVo> listByShopCodes(String jobNum, List<String> shopCodeList, int offset, int limit);
}
