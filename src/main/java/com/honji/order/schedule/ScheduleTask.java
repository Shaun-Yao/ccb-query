package com.honji.order.schedule;

import com.honji.order.HttpClientUtil;
import com.honji.order.MD5;
import com.honji.order.entity.CcbOrder;
import com.honji.order.entity.CcbPos;
import com.honji.order.service.ICcbOrderService;
import com.honji.order.service.ICcbPosService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class ScheduleTask {

    @Autowired
    private ICcbOrderService ccbOrderService;
    @Autowired
    private ICcbPosService ccbPosService;


    @Async
    @Scheduled(cron = "0 0 7 * * ?")  //
    //@Scheduled(fixedDelay = 1000000)  //间隔1秒
    public void first() throws InterruptedException {
        List<CcbPos> ccbPosList = ccbPosService.list();
        List<CcbOrder> ccbOrders = new ArrayList<>();
        //System.out.println("==ccbOrders==" + ccbPosList.getRecords().size());
        LocalDate yesterday = LocalDate.now().minusDays(1);
//        save(ccbPosList.get(0), yesterday, ccbOrders);
//        save(ccbPosList.get(1), yesterday, ccbOrders);


        for (CcbPos ccbPos : ccbPosList) {
            save(ccbPos, yesterday, ccbOrders);
            Thread.sleep(10000);
        }

        boolean result = ccbOrderService.saveBatch(ccbOrders);
        if (result) {
            log.info("{} {} 条账单保存成功", yesterday, ccbOrders.size());
        } else {
            log.error("{} 记录保存失败", yesterday, ccbOrders.size());
        }


    }

    private void save(CcbPos ccbPos, LocalDate date, List<CcbOrder> ccbOrders) {

        String MERCHANTID ="105000151314578";
        String BRANCHID="441000000";
        String POSID = ccbPos.getPosId();
        String ORDERDATE = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String BEGORDERTIME="00:00:00";
        String ENDORDERTIME="23:59:59";
        String BEGORDERID="";
        String ENDORDERID="";
        String QUPWD="Honji.123";
        String TXCODE="410408";
        String SEL_TYPE="3";
        String OPERATOR="";

        //txcode=410408
        String TYPE="0";
        String KIND="1";
        String STATUS="1";
        String ORDERID = "";
        String PAGE = "1";
        String CHANNEL = "";
        String bankURL="https://ibsbjstar.ccb.com.cn/app/ccbMain";

        String param ="MERCHANTID="+MERCHANTID+"&BRANCHID="+BRANCHID+"&POSID="+POSID+"&ORDERDATE="+ORDERDATE+"&BEGORDERTIME="+BEGORDERTIME
                +"&ENDORDERTIME="+ENDORDERTIME+"&BEGORDERID="+BEGORDERID+"&ENDORDERID="+ENDORDERID+"&QUPWD=&TXCODE="+TXCODE
                +"&SEL_TYPE="+SEL_TYPE+"&OPERATOR="+OPERATOR;

        if("410408".equals(TXCODE)){
            param ="MERCHANTID="+MERCHANTID+"&BRANCHID="+BRANCHID+"&POSID="+POSID+"&ORDERDATE="
                    +ORDERDATE+"&BEGORDERTIME="+BEGORDERTIME+"&ENDORDERTIME="+ENDORDERTIME+"&ORDERID="
                    +ORDERID+"&QUPWD=&TXCODE="+TXCODE+"&TYPE="+TYPE+"&KIND="+KIND+"&STATUS="+STATUS+
                    "&SEL_TYPE="+SEL_TYPE+"&PAGE="+PAGE+"&OPERATOR="+OPERATOR+"&CHANNEL="+CHANNEL;
        }


        Map map = new HashMap();
        map.put("MERCHANTID",MERCHANTID);

        map.put("BRANCHID",BRANCHID);
        map.put("POSID",POSID);

        map.put("ORDERDATE",ORDERDATE);

        map.put("BEGORDERTIME",BEGORDERTIME);

        map.put("ENDORDERTIME",ENDORDERTIME);

        map.put("BEGORDERID",BEGORDERID);

        map.put("ENDORDERID",ENDORDERID);

        map.put("QUPWD",QUPWD);

        map.put("TXCODE",TXCODE);

        if("410408".equals(TXCODE)){
            map.put("TYPE",TYPE);
            map.put("KIND",KIND);
            map.put("STATUS",STATUS);
            map.put("ORDERID",ORDERID);
            map.put("PAGE",PAGE);
            map.put("CHANNEL",CHANNEL);
        }

        map.put("SEL_TYPE",SEL_TYPE);
        map.put("OPERATOR",OPERATOR);
        map.put("MAC",MD5.md5Str(param));

        String ret = HttpClientUtil.httpPost(bankURL, map);
        //System.out.println(ret);
        //LocalDate date = LocalDate.parse(ORDERDATE, DateTimeFormatter.ofPattern("yyyyMMdd"));

        Document document = null;
        try {
            document = DocumentHelper.parseText(ret.trim());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        String returnCode = root.elementText("RETURN_CODE");
        //System.out.println("---returnCode===" + returnCode);


        if ("000000".equals(returnCode)) {//返回成功
            int payAmount = (int)Float.parseFloat(root.elementText("PAYAMOUNT"));
            int refundAmount = (int)Float.parseFloat(root.elementText("REFUNDAMOUNT"));
            CcbOrder ccbOrder =  new CcbOrder(ccbPos.getKhdm(), date, payAmount, refundAmount);
            ccbOrders.add(ccbOrder);
        } else {
            String returnMsg = root.elementText("RETURN_MSG");
            //System.out.println("---fail===" + POSID + "  msg===" + returnMsg);
            log.warn("posid:{} 读取日期{} 接口失败原因： {}", POSID, date, returnMsg);
        }

    }

}
