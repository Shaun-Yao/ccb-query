package com.honji.order.service;

import com.honji.order.entity.DouyinOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.honji.order.model.DaRenVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yao
 * @since 2022-08-25
 */
public interface IDouyinOrderService extends IService<DouyinOrder> {

    List<DaRenVO> listAll();
}
