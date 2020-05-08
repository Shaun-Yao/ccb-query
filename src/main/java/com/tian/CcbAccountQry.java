package com.tian;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CcbAccountQry
{
    
    //@SuppressWarnings("unchecked")
	public static void main(String[] args)throws Exception     
    {
        LocalDate date = LocalDate.of(2020, 5, 6);

        test("043783243", date, 1);
        /*
        String MERCHANTID ="105000151314578";
        String BRANCHID="441000000";                 //���д���
        String POSID="043783249";                    //��̨��
        String ORDERDATE="20200419";                  //��������
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
        String PAGE = "2";
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
        
        //if("410408".equals(TXCODE)){
        	 map.put("TYPE",TYPE);
             map.put("KIND",KIND);
             map.put("STATUS",STATUS);
             map.put("ORDERID",ORDERID);
             map.put("PAGE",PAGE);
             map.put("CHANNEL",CHANNEL);
        //}
       
        
        map.put("SEL_TYPE",SEL_TYPE);
        
        map.put("OPERATOR",OPERATOR);
        
        map.put("MAC",MD5.md5Str(param));
        
        String ret = HttpClientUtil.httpPost(bankURL, map);
        System.out.println(ret);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(format.parse(ORDERDATE).getTime());
        //persist(POSID, date, ret);
*/
    }

    private static void test(String posId, LocalDate date, int page) {
        String MERCHANTID ="105000151314578";
        String BRANCHID="441000000";                 //���д���
        String POSID=posId;           //��̨��
        String ORDERDATE="20200419";                  //��������
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
        String PAGE = String.valueOf(page);
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

        //if("410408".equals(TXCODE)){
        map.put("TYPE",TYPE);
        map.put("KIND",KIND);
        map.put("STATUS",STATUS);
        map.put("ORDERID",ORDERID);
        map.put("PAGE",PAGE);
        map.put("CHANNEL",CHANNEL);
        //}


        map.put("SEL_TYPE",SEL_TYPE);

        map.put("OPERATOR",OPERATOR);

        map.put("MAC",MD5.md5Str(param));

        String ret = HttpClientUtil.httpPost(bankURL, map);
        System.out.println(ret);
    }

    private static void persist(String posId, Date date, String ret) {
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
            System.out.println("ret::"+ payAmount);
            System.out.println("ret::"+ refundAmount);

            Connection connection = null;
            PreparedStatement ps = null;
            String sql = "INSERT INTO ccb_order(id, khdm, date, pay_amount, refund_amount) VALUES (newid(), ?, ?, ?, ?)";

            ResultSet rs = null;
            try{

                //加载驱动
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                //获取连接
                connection = DriverManager.getConnection("jdbc:sqlserver://10.10.10.155;databaseName=HonjiNav_plus", "sa", "1qazxsw2~");

                //3.发送sql语句
                ps =connection.prepareStatement(sql);

                ps.setString(1, posId);
                ps.setDate(2, date);
                ps.setInt(3, payAmount);
                ps.setInt(4, refundAmount);
                int res = ps.executeUpdate();//执行sql语句
                if(res > 0){
                    System.out.println("数据录入成功");
                }
                /*rs = ps.executeQuery();

                while (rs.next()){
                    System.out.println("name:"+rs.getString(2));
                }
*/

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
}
