package com.cn.iputils;


import com.cn.utils.DBmanager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 89IP
 * http://www.89ip.cn/tqdl.html?api=1&num=1000&port=&address=&isp=
 */
public class SixIPUtil {



    public synchronized int getIp(int size){
        delete();
        int count=0;
        String page_url = "http://www.89ip.cn/tqdl.html?api=1&num=1000&port=&address=&isp=";//提取1000条
//        String page_url = "http://www.89ip.cn/tqdl.html?num=5000&address=&kill_address=&port=&kill_port=&isp=";//提取5000条  也是1000条
        try {
            Document document = Jsoup.connect(page_url).timeout(5000).get();
            //��ȡ���� 69�� ��7ҳ
            Elements items = document.select("body");
            count=forEachData(items);
        } catch (IOException e) {
            e.printStackTrace();
            SixIPUtil sip=new SixIPUtil();
            sip.getIp(size);
        }
        return count;
    }

    //�����
/*    public synchronized int getIp(int size) {
        int count = 0;

        Document document = null;
        for (int i = 0; i < 10; i++) {
            try {
                document = Jsoup.connect("https://www.kuaidaili.com/free/inha/" + (i + 1) + "/").timeout(5000).get();
                //��ȡ���� 69�� ��7ҳ
                Elements items = document.select("div[id=list] table tbody tr");
                count = forEachDataKDL(items);
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(i);
         *//*   e.printStackTrace();
            SixIPUtil sip=new SixIPUtil();
            sip.getIp(size);*//*
            }
        }
        return count;
    }*/

    //��ץȡ����ip �浽���ݿ���
    public  int forEachDataKDL(Elements items){
        int count =0;
        Connection conn = DBmanager.open();
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO proxypool(IPAddress,IPPort,Address) VALUES(?,?,'89IP')");

        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());
            for(Element item: items){
                //����
                String title =item.select("td[data-title=ip]").text();
                String port =item.select("td[data-title=PORT]").text();
                ps.setString(1,title);
                ps.setString(2,port);
                ps.addBatch();
                count++;
            }
            ps.executeBatch();
            conn.commit();
            DBmanager.close(conn, ps,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }



    //存入数据库
    public  int forEachData(Elements items){
        int count =0;
        Connection conn = DBmanager.open();
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO proxypool(IPAddress,IPPort,Address) VALUES(?,?,'89IP')");

        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());
            for(Element item: items){
                //����
                Element mr = item.select("body").get(0);
                String title=mr.text();
                String[] ip=title.split(" ");
                for (int i=0;i<ip.length;i++){
                    ps.setString(1,ip[i].split(":")[0]);
                    ps.setString(2,ip[i].split(":")[1]);
                    ps.addBatch();
                    count++;
                }
            }
            ps.executeBatch();
            conn.commit();
            DBmanager.close(conn, ps,null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    //数据库表清除功能(id也一并清除)
    public static void delete() {
        try(Connection conn = DBmanager.open();
            Statement statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE proxypool");
            statement.close();
            conn.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
