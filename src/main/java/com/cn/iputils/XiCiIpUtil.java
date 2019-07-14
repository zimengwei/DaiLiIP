package com.cn.iputils;

import com.cn.utils.DBmanager;
import com.cn.utils.HttpsUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 西刺代理
 * 一次只能取到 20页  *10 2000条数据
 * https://www.xicidaili.com/nn/1
 */
@Component
public class XiCiIpUtil {
    private String startUrl = "https://www.xicidaili.com/nn";//   .com/nn/1....n

    public void doXiCiIpUtil() {
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
        for (int i = 1; i <= 20; i++) {//取20页数据
            //解析页面的数据
            Document document = Jsoup.connect(html+"/"+i).timeout(6000).get();
            System.out.println(document.baseUri());
            Elements items = document.select("#ip_list").get(0).select("tr");
            forEachData(items);
            Thread.sleep(2000);//休眠2秒
        }

    }

    //5)把抓取到的ip 存到数据库里
    public int forEachData(Elements items) {
        //delete();//先删除表内数据
        int count = 0;
        Connection conn = DBmanager.open();
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO Proxypool(IPAddress,IPPort,serverAddress,IPType,IPSpeed,Address) VALUES(?,?,?,?,?,'西刺代理')");
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());

            for (int i = 1; i < items.size(); i++) {//每页显示101条数据  从第一条开始
                Elements elements = items.get(i).select("td");
                ps.setString(1, elements.get(1).text());//IP
                ps.setString(2, elements.get(2).text());//端口
                ps.setString(3, elements.get(3).text());//服务器类型
                ps.setString(4, elements.get(5).text());//类型
                ps.setString(5, elements.get(6).select("div").get(0).attributes().get("title"));//速度
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

    //数据库表清除功能(id也一并清除)
    public static void delete() {
        try (
                Connection conn = DBmanager.open();
                Statement statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE Proxypool");
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
