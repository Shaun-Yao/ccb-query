package com.honji.order;

import com.honji.order.service.IDailyDepositService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.honji.order.mapper")
public class CcbQueryApplication  {

    @Autowired
    private IDailyDepositService dailyDepositService;

    public static void main(String[] args) {
        SpringApplication.run(CcbQueryApplication.class, args);

    }
/*

    @PostConstruct
    public void loadImage() {
       DailyDeposit deposit = dailyDepositService.getById(1);


            StringBuffer uri = new StringBuffer("\\\\10.10.10.188\\183财务存款票据\\");

            uri.append(deposit.getKhdm()).append(".").append(deposit.getDate())
                    .append(".").append(deposit.getId()).append(".jpg");


            File file = new File(uri.toString());

//            OutputStream outputStream = null;
            try {
                FileUtils.writeByteArrayToFile(file, deposit.getPicture(), false);
//                outputStream = new FileOutputStream(file);
                System.out.println("2222");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
*/

  /*  @PostConstruct
    public void loadImages() {
        QueryWrapper<DailyDeposit> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("khdm", "Z75320");
        queryWrapper.ge("date", "2020-07-16");
        queryWrapper.le("date", "2020-07-22");
        queryWrapper.isNotNull("picture");

        List<DailyDeposit> deposits = dailyDepositService.list(queryWrapper);

        for (DailyDeposit deposit : deposits) {
//            if (deposit.getDate().isBefore(LocalDate.of(2020, 7, 5))) {
//                continue;
//            }
            StringBuffer uri = new StringBuffer("E:\\deposit\\");

            uri.append(deposit.getKhdm()).append(".").append(deposit.getDate())
                    .append(".").append(deposit.getId()).append(".jpg");


            File file = new File(uri.toString());

//            OutputStream outputStream = null;
            try {
                FileUtils.writeByteArrayToFile(file, deposit.getPicture(), false);
//                outputStream = new FileOutputStream(file);
//                //blob.getBytes的第一个参数是从第几个字节开始提取数据，第二个是提取字节的长度
//                outputStream.write(deposit.getPicture());
//                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                *//*try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }*//*
            }
        }


    }*/

}
