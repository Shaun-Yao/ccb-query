package com.honji.order;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class QrDemo {
	
	public static void main(String[] args)throws Exception  {
		
		String bankURL="https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain";
	    bankURL="http://128.128.97.200:8101/CCBIS/ccbMain";
	        
		String MERCHANTID = "105421097080009";
        String POSID = "902807340";
        String BRANCHID = "360000000";
        String ORDERID = "111111111111";
        String PAYMENT= "0.01";
        String CURCODE="01";
        String TXCODE = "530550";
        String REMARK1 = "";
        String REMARK2 = "";
        String RETURNTYPE="1";
        String TIMEOUT = "";
        String PUB32TR2= "f6528d5c335b7092fc9ec1b3020111";
        
        StringBuffer tmp = new StringBuffer(); //��ǩ�ֶ�
        tmp.append("MERCHANTID=");
        tmp.append(MERCHANTID);
        tmp.append("&POSID=");
        tmp.append(POSID);
        tmp.append("&BRANCHID=");
        tmp.append(BRANCHID);
        tmp.append("&ORDERID=");
        tmp.append(ORDERID);
        tmp.append("&PAYMENT=");
        tmp.append(PAYMENT);
        tmp.append("&CURCODE=");
        tmp.append(CURCODE);
        tmp.append("&TXCODE=");
        tmp.append(TXCODE);
        tmp.append("&REMARK1=");
        tmp.append(REMARK1);
        tmp.append("&REMARK2=");
        tmp.append(REMARK2);
        tmp.append("&RETURNTYPE=");
        tmp.append(RETURNTYPE);
        tmp.append("&TIMEOUT=");
        tmp.append(TIMEOUT);
        tmp.append("&PUB=");
        tmp.append(PUB32TR2);
     
        Map map = new HashMap();
        map.put("CCB_IBSVersion","V6");	//������
        map.put("MERCHANTID",MERCHANTID);
        map.put("BRANCHID",BRANCHID);
        map.put("POSID",POSID);
        map.put("ORDERID",ORDERID);
        map.put("PAYMENT",PAYMENT);
        map.put("CURCODE",CURCODE);
        map.put("TXCODE",TXCODE);
        map.put("REMARK1",REMARK1);
        map.put("REMARK2",REMARK2);
        map.put("RETURNTYPE",RETURNTYPE);
        map.put("TIMEOUT",TIMEOUT);
        map.put("MAC",MD5.md5Str(tmp.toString()));
        
        String ret = HttpClientUtil.httpPost(bankURL, map); 	//�����ά���������Ӵ�
        System.out.println("ret::"+ret);
        
        Gson gson = new Gson();
        QrURLDemo qrurl = (QrURLDemo) gson.fromJson(ret, QrURLDemo.class);
        System.out.println(qrurl.getPAYURL());
        ret = HttpClientUtil.httpGet(qrurl.getPAYURL(), "UTF-8"); //��ȡ��ά�봮
        System.out.println("ret::"+ret);
    }
}
