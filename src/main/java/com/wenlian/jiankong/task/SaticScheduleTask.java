package com.wenlian.jiankong.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class SaticScheduleTask {
    @Autowired
    private JavaMailSender javaMailSender;
    private static URL url;
    private static HttpURLConnection con;
    private static int state = -1;
    public static String[]  urls = {"ningbo","yunnan","jiangxi","hunan","shandong","qinghai","neimenggu","xinjiang",
            "guizhou","zhejiang","liaoning","bingtuanwl","heilongjiang","hebei","ningxia","gansu"};
    public static  List<String> list= new ArrayList<>(Arrays.asList(urls));
    //3.添加定时任务
    @Scheduled(cron = "0 0/15 * * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
       Iterator<String> iterator=list.iterator();
       while (iterator.hasNext())
       {
           String s=iterator.next();
           try {
               url = new URL("http://hygl.artnchina.com/"+s+"-coll");
               con = (HttpURLConnection) url.openConnection();
               state = con.getResponseCode();
               System.out.println(s +" responseCode="+state);
               if (state == 200) {
                   System.out.println(s+" URL可用！");
               }else
               {
                   System.out.println(s+" URL不可用!");
                   SimpleMailMessage message = new SimpleMailMessage();
                   message.setFrom("1253926422@qq.com");
                   message.setTo("1253926422@qq.com");
                   message.setSubject("文联网卡异常");
                   message.setText(s+" URL不可用!"+state);
                   javaMailSender.send(message);
                   iterator.remove();
               }
           }catch (Exception ex) {
               System.out.println(ex.getMessage());
               SimpleMailMessage message = new SimpleMailMessage();
               message.setFrom("1253926422@qq.com");
               message.setTo("1253926422@qq.com");
               message.setSubject("文联网卡异常");
               message.setText(s+" 出错了!"+ex.getMessage());
               javaMailSender.send(message);
               message.setTo("w2538525@163.com");
               javaMailSender.send(message);
               iterator.remove();
           }
           finally {
               con.disconnect();
           }
       }
    }

}
