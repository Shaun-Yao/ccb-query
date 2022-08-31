package com.honji.order.service.impl;

import com.honji.order.entity.DouyinOrder;
import com.honji.order.mapper.DouyinOrderMapper;
import com.honji.order.model.DaRenVO;
import com.honji.order.service.IDouyinOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2022-08-25
 */
@Service
public class DouyinOrderServiceImpl extends ServiceImpl<DouyinOrderMapper, DouyinOrder> implements IDouyinOrderService {

    @Autowired
    private DouyinOrderMapper orderMapper;

    @Override
    public List<DaRenVO> listAll() {
        return orderMapper.selectAll();
    }
}
