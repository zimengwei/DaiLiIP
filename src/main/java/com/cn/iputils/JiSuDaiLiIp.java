package com.cn.iputils;

import com.cn.utils.DBmanager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 极速代理IP
 * 10*20  200条数据
 * http://www.superfastip.com/welcome/freeip/1
 */
@Component
public class JiSuDaiLiIp {
    private String startUrl = "http://www.superfastip.com/welcome/freeip/";

    public void doJiSuDaiLiIp(){
        try {
            parseHtml(startUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析html页面
     * @param html
     */
    private void parseHtml(String html) throws Exception {
        for (int i = 1; i <= 10; i++) {
            //解析页面的数据
            Document document = Jsoup.connect(html+i).timeout(6000).get();
            System.out.println(document.baseUri());
            Elements items = document.select(".table").get(1).select("tbody").get(0).select("tr");
            forEachData(items);
        }
    }
    //)把抓取到的ip 存到数据库里
    public int forEachData(Elements items) {
        int count = 0;
        Connection conn = DBmanager.open();
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO Proxypool(IPAddress,IPPort,IPType,IPSpeed,Address) VALUES(?,?,?,?,'极速代理')");
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());

            for (int i = 0; i < items.size(); i++) {//每页显示20条数据  从第一条开始
                Elements elements = items.get(i).select("td");
                ps.setString(1, elements.get(0).text());//IP
                ps.setString(2, elements.get(1).text());//端口
                ps.setString(3, elements.get(3).text());//服务器类型
                ps.setString(4, elements.get(5).text());//速度
                ps.addBatch();
                count++;
                System.out.println(count + "--" + ps + "抓取成功");
            }
            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBmanager.close(conn, ps, null);
        }
        return count;
    }
}
