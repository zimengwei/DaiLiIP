package com.cn.iputils;

import com.cn.utils.DBmanager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 西拉代理 IP
 * 每页显示50    50* 20   1000条
 * http://www.xiladaili.com/gaoni/1/
 *
 */
@Component
public class XiLaDaiLiIp {
    private String startUrl = "http://www.xiladaili.com/gaoni/";

    public void doXiLaDaiLiIp(){
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
        for (int i = 1; i <= 20; i++) {  //50* 20   1000条
            //解析页面的数据
            Thread.sleep(2000);//休眠2秒
            Document document = Jsoup.connect(html+i+"/").timeout(6000).get();
            System.out.println(document.baseUri());
            Elements items = document.select("table").select("tbody").select("tr");
            forEachData(items);
        }
    }
    //)把抓取到的ip 存到数据库里
    public int forEachData(Elements items) {
        int count = 0;
        Connection conn = DBmanager.open();
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO Proxypool(IPAddress,IPPort,IPType,IPSpeed,Address) VALUES(?,?,?,?,'西拉代理')");
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());

            for (int i = 0; i < items.size(); i++) {//每页显示20条数据  从第一条开始
                Elements elements = items.get(i).select("td");
                String el = elements.get(0).text();
                ps.setString(1, el.substring(0, el.indexOf(":")));//IP
                ps.setString(2, el.substring(el.indexOf(":")+1));//端口
                ps.setString(3, elements.get(1).text());//代理协议
                ps.setString(4, elements.get(4).text());//速度
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
