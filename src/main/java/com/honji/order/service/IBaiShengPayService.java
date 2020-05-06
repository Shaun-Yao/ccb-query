package com.honji.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.order.entity.BaiShengPay;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2020-05-05
 */
public interface IBaiShengPayService extends IService<BaiShengPay> {

    boolean fastSaveBatch(List<BaiShengPay> list);
}
