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
import com.honji.order.model.DepositVo;
import com.honji.order.service.IDailyDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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
    public PageInfo<DepositVo> listByCurrentUser(String shopCode, int offset, int limit) {
//        String user = (String) session.getAttribute("user");
        PageHelper.startPage(offset / limit + 1, limit);
        List<DepositVo> depositVos = dailyDepositMapper.selectByShopCode(shopCode);
        //Page<DepositVo> depositVoPage = dailyDepositMapper.selectByShopCode("Z75320");
        return new PageInfo<>(depositVos);
    }

    @Override
    public PageInfo<DepositVo> listByShopCodes(DepositDTO depositDTO, List<String> shopCodeList) {
//        String user = (String) session.getAttribute("user");
//        String[] shopCodes = {"Z59573", "Z57113"};
        //List<String> shopCodes = Arrays.asList("Z59573", "Z57113");

        String shopCodes = null;
        //没有查询条件显示当前用户负责的所有门店的存款信息
        if (shopCodeList == null || shopCodeList.isEmpty()) {
            QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_num", depositDTO.getJobNum());
//        StringBuffer shopCodes = new StringBuffer();
            //根据工号查询负责的门店，再转为
            List<Authority> authorities = authorityMapper.selectList(queryWrapper);
            List<String> shopCodeList2 = authorities.stream().map(e -> e.getKhdm()).collect(Collectors.toList());
            shopCodes = shopCodeList2.stream().map(s -> "\'" + s + "\'")
                    .collect(Collectors.joining(", "));
        } else {//按条件查询
            shopCodes = shopCodeList.stream().map(s -> "\'" + s + "\'")
                    .collect(Collectors.joining(", "));
        }

        PageHelper.startPage(depositDTO.getOffset() / depositDTO.getLimit() + 1, depositDTO.getLimit());
        List<DepositVo> depositVos = dailyDepositMapper.selectByShopCodes(shopCodes, depositDTO);
//        QueryWrapper<DailyDeposit> qw = new QueryWrapper<DailyDeposit>();
//        qw.eq("date", "2020-08-01");
//        List<DailyDeposit> dailyDeposits = dailyDepositMapper.selectList(qw);
//        System.out.println("=====" + dailyDeposits.size());

//        List<DepositVo> depositVos = dailyDepositMapper.selectByShopCode("2020-09-25");
        //Page<DepositVo> depositVoPage = dailyDepositMapper.selectByShopCode("Z59568");
//        return null;
        return new PageInfo<>(depositVos);
    }
}
