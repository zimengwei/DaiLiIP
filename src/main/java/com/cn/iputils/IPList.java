package com.cn.iputils;


import com.cn.utils.DBmanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 对IP进行操作
 */
public class IPList {
    private static  List<IPBean> ipBeanList = new ArrayList<>();

    // 计数器,线程结束即+1, 用于判断所有副线程是否完成
    private static int count = 0;

    /**
     * 支持并发操作
     *
     * @param bean
     */
    public static synchronized void add(IPBean bean) {
        ipBeanList.add(bean);
    }

    public static int getSize() {
        return ipBeanList.size();
    }


    public static synchronized void increase() {
        count++;
    }

    public static synchronized int getCount(){
        return count;
    }
    public static synchronized List<IPBean> getIpBeanList(){
        ipBeanList.remove(0);
        return ipBeanList;
    }

  /*  public static synchronized IPBean getIpBean(){
        IPBean lp=ipBeanList.get(0);
        ipBeanList.remove(0);
        return lp;
    }*/


  //取一个  删一个
    public static synchronized IPBean getIpBean(){
        IPBean ip=null;
        try(Connection conn = DBmanager.open();
            Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT id,IPAddress,IPPort FROM ProxyPool order by id LIMIT 1 ");
            while(resultSet.next()){
                ip = new IPBean();
                ip.setId(resultSet.getInt(1));
                ip.setIp(resultSet.getString(2));
                ip.setPort(resultSet.getString(3));

            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(ip!=null){
            deleteById(ip.getId());
        }
        return ip;
    }
    //取一个  删一个
    public static synchronized void deleteById(int id) {
        int s= DBmanager.getInt("count","select count(1) count from proxypool",null);
        if(s==20){
            System.out.println("重新获取ip");
            SixIPUtil sip=new SixIPUtil();
            sip.getIp(2000);
        }
        try(Connection conn = DBmanager.open();
            Statement statement = conn.createStatement()) {
            statement.executeUpdate("delete  from  proxypool where id="+id );
            statement.close();
            conn.close();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
