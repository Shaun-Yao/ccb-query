package com.honji.order.schedule;

import com.honji.order.HttpClientUtil;
import com.honji.order.MD5;
import com.honji.order.entity.CcbPos;
import com.honji.order.service.ICcbPosService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
@EnableScheduling
@EnableAsync
public class ScheduleTask {

//    @Autowired
//    private ICcbOrderService ccbOrderService;
    @Autowired
    private ICcbPosService ccbPosService;

    private static final int SLEEP_TIME = 20 * 1000;

/*

    @PostConstruct
    public void backup() throws InterruptedException {
        List<CcbPos> ccbPosList = ccbPosService.list();

        LocalDate yesterday = LocalDate.of(2020, 4, 25);
        LocalDate date = LocalDate.of(2020, 4, 1);
        while (date.isBefore(yesterday)) {
            List<CcbOrder> ccbOrders = new ArrayList<>();
            for (CcbPos ccbPos : ccbPosList) {
                save(ccbPos.getKhdm(), ccbPos.getPosId(), date, 1);
                Thread.sleep(10000);
            }
//            save(ccbPosList.get(0), date, ccbOrders);
//            save(ccbPosList.get(1), date, ccbOrders);
//            save(ccbPosList.get(2), date, ccbOrders);

            date = date.plusDays(1);
        }
    }
*/


//    @Async
//    @Scheduled(cron = "0 0 7 * * ?")  //
    //@Scheduled(fixedDelay = 1000000000)  //间隔1秒
    public void first() throws InterruptedException {
        List<CcbPos> ccbPosList = ccbPosService.list();

        //System.out.println("==ccbOrders==" + ccbPosList.getRecords().size());
        LocalDate yesterday = LocalDate.now().minusDays(1);
//        LocalDate date = LocalDate.of(2020, 5, 9);
//        LocalDate date1 = LocalDate.of(2020, 5, 10);
//        save("ss", "043783249", date, 1);
//        save("ss", "043783249", date, 2);
        /*
        for (CcbPos ccbPos : ccbPosList) {
            if ("043783249".equals(ccbPos.getPosId())) {

                save(ccbPos.getKhdm(),ccbPos.getPosId(), date, 1);
            }
        }
*/

//        for (CcbPos ccbPos : ccbPosList) {
//            save(ccbPos.getKhdm(), ccbPos.getPosId(), yesterday, 1);
//            Thread.sleep(SLEEP_TIME);
//        }

    }


/*

    private void save(String khdm, String posId, LocalDate date, int page) throws InterruptedException {

        String result =  request(posId, date, page);
        //LocalDate date = LocalDate.parse(ORDERDATE, DateTimeFormatter.ofPattern("yyyyMMdd"));

        Document document = null;
        try {
            document = DocumentHelper.parseText(result.trim());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        String returnCode = root.elementText("RETURN_CODE");

        if ("000000".equals(returnCode)) {//返回成功
            List<CcbOrder> ccbOrders = new ArrayList<>();

            Iterator<Element> orders = root.elementIterator("QUERYORDER");
            while(orders.hasNext()) {
                Element e = orders.next();
                double amount = Float.parseFloat(e.elementText("AMOUNT")) ;
                String num = e.elementText("ORDERID");
                int type = 1;
                if (num.startsWith("0")) {//0开头是被扫
                    type = 2;
                }
                double fee = Double.parseDouble(String.format("%.2f", amount * 0.0025));//手续费固定0.0025，四舍五入保留两倍小数
//                CcbOrder ccbOrder = new CcbOrder(khdm, date, amount, fee, num, type);
//                ccbOrders.add(ccbOrder);
            }

            boolean saveResult = false;
            try {
                saveResult = ccbOrderService.saveBatch(ccbOrders);
            } catch (Exception e) {//可能出现重复订单号问题
                log.error("{} {} 页码{} {}条记录保存出现异常", khdm, date, page, ccbOrders.size());
                //e.printStackTrace();
            }

            if (!saveResult) {
                log.error("{} {} 页码{} {}条记录保存失败", khdm, date, page, ccbOrders.size());
            }

            int pageCount = Integer.parseInt(root.elementText("PAGECOUNT"));
            if (pageCount != page) {//数据超过1页
                Thread.sleep(SLEEP_TIME);
                page ++;//页码加1
                save(khdm, posId, date, page);//递归
            }
        } else {
            String returnMsg = root.elementText("RETURN_MSG");
            log.warn("posid:{} 日期{} 页码{} 返回失败：{}", posId, date, page, returnMsg);
        }

    }
*/

    private String request(String posId, LocalDate date, int page) {
        String MERCHANTID ="105000151314578";
        String BRANCHID="441000000";
        String POSID = posId;
        String ORDERDATE = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String BEGORDERTIME="00:00:00";
        String ENDORDERTIME="23:59:59";
        String BEGORDERID="";
        String ENDORDERID="";
        String QUPWD="Honji.123";
        final String TXCODE="410408";
        String SEL_TYPE="3";
        String OPERATOR="";

        //txcode=410408
        String TYPE="0";
        String KIND="1";
        String STATUS="1";
        String ORDERID = "";
        String PAGE = String.valueOf(page);//string必须转为字符串，后面要做参数拼接
        String CHANNEL = "";
        String bankURL="https://ibsbjstar.ccb.com.cn/app/ccbMain";

        String param ="MERCHANTID="+MERCHANTID+"&BRANCHID="+BRANCHID+"&POSID="+POSID+"&ORDERDATE="
                +ORDERDATE+"&BEGORDERTIME="+BEGORDERTIME+"&ENDORDERTIME="+ENDORDERTIME+"&ORDERID="
                +ORDERID+"&QUPWD=&TXCODE="+TXCODE+"&TYPE="+TYPE+"&KIND="+KIND+"&STATUS="+STATUS+
                "&SEL_TYPE="+SEL_TYPE+"&PAGE="+PAGE+"&OPERATOR="+OPERATOR+"&CHANNEL="+CHANNEL;

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

        map.put("TYPE",TYPE);
        map.put("KIND",KIND);
        map.put("STATUS",STATUS);
        map.put("ORDERID",ORDERID);
        map.put("PAGE",PAGE);
        map.put("CHANNEL",CHANNEL);


        map.put("SEL_TYPE",SEL_TYPE);
        map.put("OPERATOR",OPERATOR);
        map.put("MAC",MD5.md5Str(param));

        String ret = HttpClientUtil.httpPost(bankURL, map);

        return ret;
    }

    public static void main(String[] args) {
//        double num = Math.round(332 * 0.25) * 0.01d;
//        System.out.println(num);
//        double fee = Double.parseDouble(String.format("%.2f", 70 * 0.0025));//手续费固定0.0025，四舍五入保留两倍小数
//        System.out.println(fee);
        System.out.println("030332".startsWith("0"));
    }

}
