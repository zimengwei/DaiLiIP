package com.cn.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBmanager {

	public static  Connection open() {
        Map<String, String> conn = null;
		String url =GradConfigure.URL;
		String user = GradConfigure.USER;
		String password =  GradConfigure.PASSWORD;
		Connection connection = null;
		try {
			Class.forName(GradConfigure.DRIVER);
			connection = DriverManager.getConnection(url,"root","root");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static void close(Connection conn, Statement stmt, ResultSet rset) {
		try {
			if (rset != null) {
				rset.close();
				rset = null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		if (conn != null)
			try {
				conn.close();
                conn = null;
			} catch (SQLException e) {
                e.printStackTrace(System.err);
			}
	}

    /**
     *
     * @param sql:prepareStatement
     * @return
     */
    public static List<HashMap> getDataList(List lables, String sql, Map params) {
        Connection conn = open();
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<HashMap> back = new ArrayList<HashMap>();
        try {
            ps = conn.prepareStatement(sql);
            if(params!=null){
                for (int i = 1; i <= params.size(); i++) {
                    ps.setObject(i, params.get(i));
                }
            }
            rs = ps.executeQuery();
            while(rs.next()){
                HashMap map = new HashMap();
                for (int i = 0; i < lables.size(); i++) {
                    map.put(lables.get(i).toString(),rs.getObject(lables.get(i).toString()));
                }
                back.add(map);
            }
        } catch (Exception e) {
                System.out.println(sql);
        } finally{
            close(conn, ps, rs);
        }
        return back;
    }


    public HashMap<String, String> getAllSSGS() {
        String sql=" select ZQDM from company_information group by  ZQDM  ";
        HashMap<String, String> map = new HashMap<String, String>();
        Connection conn=null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = open();
            stmt =conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String ZQDM = rs.getString(1);
                map.put(ZQDM, ZQDM==null?"":ZQDM.toString());
            }
        } catch (Exception e) {
        } finally {
            close(conn, stmt, rs);
        }
        return map;
    }


    public HashMap<String, String> getAllJYFX() {
        String sql=" select ZQDM from business_analysis group by  ZQDM  ";
        HashMap<String, String> map = new HashMap<String, String>();
        Connection conn=null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = open();
            stmt =conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String ZQDM = rs.getString(1);
                map.put(ZQDM, ZQDM==null?"":ZQDM.toString());
            }
        } catch (Exception e) {
        } finally {
            close(conn, stmt, rs);
        }
        return map;
    }

    /**
     * 查询 公司信息表  GSXX
     * @return
     */
    public List<HashMap<String, Object>> getAllCustomer() {
        List list = new ArrayList<HashMap<String, Object>>();

        String sql="SELECT id,GSQC,GSJC,GFDM FROM GSXX ORDER BY id";
        Connection conn=null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = open();
            stmt =conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                String id = rs.getString(1);
                String GSQC = rs.getString(2);
                String GSJC = rs.getString(3);
                String GFDM = rs.getString(4);
                map.put("id", id==null?"":id.toString());
                map.put("GSQC", GSQC==null?"":GSQC.toString());
                map.put("GSJC", GSJC==null?"":GSJC.toString());
                map.put("GFDM", GFDM==null?"":GFDM.toString());
                list.add(map);
            }
        } catch (Exception e) {
        } finally {
            close(conn, stmt, rs);
        }
        return list;
    }
    public List<HashMap<String, Object>> getAllCustomers() {
        int i=0;
        List listAll=new ArrayList();
        List list = new ArrayList<HashMap<String, Object>>();
        String sql="select id,zqdm from client order by zqdm";
        Connection conn=null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = open();
            stmt =conn.prepareStatement(sql.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                String id = rs.getString(1);
                String zqdm = rs.getString(2);
                map.put("id", id==null?"":id.toString());
                map.put("zqdm", zqdm==null?"":zqdm.toString());
                list.add(map);
                i++;
                if(i==600){
                    i=0;
                    listAll.add(list);
                    list= new ArrayList<HashMap<String, Object>>();
                }
            }
            if(list.size()!=600){
                listAll.add(list);
            }
        } catch (Exception e) {
        } finally {
            close(conn, stmt, rs);
        }
        return listAll;
    }


    public static int getInt(String lable,String sql,Map params){
        Connection conn = open();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int str=0;
        try {
            ps = conn.prepareStatement(sql);
            if(params!=null){
                for (int i = 1; i <= params.size(); i++) {
                    ps.setString(i,params.get(i)==null?"":params.get(i).toString());
                }
            }
            rs = ps.executeQuery();
            while(rs.next()){
                str = rs.getInt(lable);
                break;
            }
        } catch (Exception e) {
                System.out.println(sql);
        } finally{
            close(conn, ps, rs);
        }
        return str;
    }
}
