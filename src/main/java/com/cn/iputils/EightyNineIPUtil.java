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
 * 89 ip获取
 * http://www.89ip.cn/tqdl.html?api=1&num=1000&port=&address=&isp=
 */
@Component
public class EightyNineIPUtil {
    private String startUrl = "http://www.89ip.cn/tqdl.html?api=1&num=1000&port=&address=&isp=";

    public void doEightyNineIPUtil() {
        try {
            //1）使用工具类创建一个HttpClient对象
            CloseableHttpClient httpClient = HttpsUtils.getHttpClient();
            //2）使用HttpClient发送post请求
            HttpPost post = new HttpPost(startUrl);
            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            //3）接收服务端响应的html
            CloseableHttpResponse response = httpClient.execute(post);
            String html = EntityUtils.toString(response.getEntity(), "utf-8");
            //4）使用jsoup解析html，解析数据
            parseHtml(html);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析html页面
     * @param html
     */
    private void parseHtml(String html) throws Exception {
        //解析页面的数据
        Document document = Jsoup.parse(html);
        Elements items = document.select("body");
        forEachData(items);
        Thread.sleep(2000);//休眠2秒
    }

    //5)把抓取到的ip 存到数据库里
    public int forEachData(Elements items) {
        //delete();//先删除表内数据
        int count = 0;
        Connection conn = DBmanager.open();
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO proxypool(IPAddress,IPPort,Address) VALUES(?,?,'89IP')");
        try {
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql.toString());
            for (Element item : items) {
                //标题
                Element mr = item.select("body").get(0);
                String title = mr.text();
                String[] ip = title.split(" ");
                for (int i = 0; i < ip.length; i++) {
                    ps.setString(1, ip[i].split(":")[0]);
                    ps.setString(2, ip[i].split(":")[1]);
                    ps.addBatch();
                    count++;
                    System.out.println(count + "--" + ps + "抓取成功");
                }
            }
            ps.executeBatch();
            conn.commit();
            DBmanager.close(conn, ps, null);
        } catch (SQLException e) {
            e.printStackTrace();
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
