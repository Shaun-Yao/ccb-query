package com.honji.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Authority;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.mapper.AuthorityMapper;
import com.honji.order.mapper.DailyDepositMapper;
import com.honji.order.model.DepositDTO;
import com.honji.order.model.DepositVO;
import com.honji.order.service.IDailyDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yao
 * @since 2020-07-20
 */
@Service
public class DailyDepositServiceImpl extends ServiceImpl<DailyDepositMapper, DailyDeposit> implements IDailyDepositService {

    @Autowired
    private DailyDepositMapper dailyDepositMapper;

    @Autowired
    private AuthorityMapper authorityMapper;

    @Autowired
    private HttpSession session;

    @Override
    public PageInfo<DepositVO> listByCurrentUser(String shopCode, int offset, int limit) {
//        String user = (String) session.getAttribute("user");
        PageHelper.startPage(offset / limit + 1, limit);
        List<DepositVO> depositVos = dailyDepositMapper.selectByShopCode(shopCode);
        //Page<DepositVO> depositVoPage = dailyDepositMapper.selectByShopCode("Z75320");
        return new PageInfo<>(depositVos);
    }

    @Override
    public PageInfo<DepositVO> listByShopCodes(DepositDTO depositDTO) {

        PageHelper.startPage(depositDTO.getOffset() / depositDTO.getLimit() + 1, depositDTO.getLimit());
        List<DepositVO> depositVos = new ArrayList<>();
        if (depositDTO.getShopCodes() != null && depositDTO.getShopCodes().size() > 0) {//店铺集合不为空才查询
            depositVos = dailyDepositMapper.selectByShopCodes(depositDTO);
        }
        return new PageInfo<>(depositVos);
    }

    @Override
    public List<DepositVO> listAll(DepositDTO depositDTO) {
        List<DepositVO> depositVos = dailyDepositMapper.selectByShopCodes(depositDTO);
        return depositVos;
    }
/*
    private String parseShopCodes(String jobNum, List<String> shopCodeList) {
        String shopCodes = null;
        //没有查询条件显示当前用户负责的所有门店的存款信息
        if (shopCodeList == null || shopCodeList.isEmpty()) {
            QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_num", jobNum);
//        StringBuffer shopCodes = new StringBuffer();
            //根据工号查询负责的门店，再拼接成字符串
            List<Authority> authorities = authorityMapper.selectList(queryWrapper);
            List<String> shopCodeList2 = authorities.stream().map(e -> e.getKhdm()).collect(Collectors.toList());
            shopCodes = shopCodeList2.stream().map(s -> "\'" + s + "\'")
                    .collect(Collectors.joining(", "));
        } else {//按条件查询
            shopCodes = shopCodeList.stream().map(s -> "\'" + s + "\'")
                    .collect(Collectors.joining(", "));
        }
        return shopCodes;
    }*/
}
