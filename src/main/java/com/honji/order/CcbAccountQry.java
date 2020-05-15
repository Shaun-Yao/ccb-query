package com.honji.order;

import com.honji.order.service.ICcbOrderService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class CcbAccountQry
{
    @Autowired
    private static ICcbOrderService ccbOrderService;
    //@SuppressWarnings("unchecked")
	public static void main(String[] args)throws Exception     
    {
        String MERCHANTID ="105000151314578";
        String BRANCHID="441000000";                 //���д���
        String POSID="043783293";                    //��̨��
        String ORDERDATE="20200514";                  //��������
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
        System.out.println(ret);
        LocalDate date = LocalDate.parse(ORDERDATE, DateTimeFormatter.ofPattern("yyyyMMdd"));
        persist(POSID, date, ret);

    }

    private static void persist(String posId, LocalDate date, String ret) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(ret.trim());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        String returnCode = root.elementText("RETURN_CODE");
        if ("000000".equals(returnCode)) {//返回成功
            int payAmount = (int)Float.parseFloat(root.elementText("PAYAMOUNT"));
            int refundAmount = (int)Float.parseFloat(root.elementText("REFUNDAMOUNT"));
            //CcbOrder ccbOrder = new CcbOrder(posId, date, payAmount, refundAmount);
            //CcbOrder ccbOrder = new CcbOrder("22", posId, date, payAmount, refundAmount);
            //ccbOrderService.save(ccbOrder);

        }
    }
    
}
