package com.honji.order.service.impl;

import com.honji.order.entity.Bank;
import com.honji.order.mapper.BankMapper;
import com.honji.order.service.IBankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2020-07-23
 */
@Service
public class BankServiceImpl extends ServiceImpl<BankMapper, Bank> implements IBankService {

}
