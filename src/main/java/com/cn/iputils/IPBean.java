package com.cn.iputils;

public class IPBean {
    public static final int TYPE_HTTP = 0;
    public static final int TYPE_HTTPS = 1;

    private int  id;
    private String ip;
    private String port;
    private int type;

    public IPBean(){

    }
    public IPBean(IPBean bean){
        ip = bean.getIp();
        port = bean.getPort();
        type = bean.getType();
    }

    public IPBean(String ip, String port, int type) {
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
