package com.honji.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.honji.order.entity.Authority;
import com.honji.order.entity.DailyDeposit;
import com.honji.order.mapper.AuthorityMapper;
import com.honji.order.mapper.DailyDepositMapper;
import com.honji.order.model.DepositVo;
import com.honji.order.service.IDailyDepositService;
import org.apache.commons.lang3.StringUtils;
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
    public PageInfo<DepositVo> listByShopCodes(String jobNum, List<String> shopCodeList, int offset, int limit) {
//        String user = (String) session.getAttribute("user");
//        String[] shopCodes = {"Z59573", "Z57113"};
        //List<String> shopCodes = Arrays.asList("Z59573", "Z57113");

        String shopCodes = null;
        if (shopCodeList == null || shopCodeList.isEmpty()) {
            QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_num", jobNum);
//        StringBuffer shopCodes = new StringBuffer();
            List<Authority> authorities = authorityMapper.selectList(queryWrapper);
            List<String> shopCodeList2 = authorities.stream().map(e -> e.getKhdm()).collect(Collectors.toList());
            shopCodes = shopCodeList2.stream().map(s -> "\'" + s + "\'")
                    .collect(Collectors.joining(", "));
        } else {
            shopCodes = shopCodeList.stream().map(s -> "\'" + s + "\'")
                    .collect(Collectors.joining(", "));
        }

        PageHelper.startPage(offset / limit + 1, limit);
        List<DepositVo> depositVos = dailyDepositMapper.selectByShopCodes(shopCodes);
        //Page<DepositVo> depositVoPage = dailyDepositMapper.selectByShopCode("Z75320");
        return new PageInfo<>(depositVos);
    }
}
