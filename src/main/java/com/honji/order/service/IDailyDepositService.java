package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;

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

    PageInfo<DepositVO> listByCurrentUser(String shopCode, int offset, int limit);
    PageInfo<DepositVO> listByShopCodes(DepositDTO depositDTO, List<String> shopCodeList);
    List<DepositVO> listAll(DepositDTO depositDTO, List<String> shopCodeList);
}
