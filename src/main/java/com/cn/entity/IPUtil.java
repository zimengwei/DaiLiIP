package com.cn.entity;

import javax.persistence.*;

@Entity
@Table(name = "AgentTool")
public class IPUtil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String IPAddress;//IP地址
    private String IPPort;//IP端口
    private String serverAddress;//服务器地址
    private String IPType;//IP类型
    private String IPSpeed;//IP速度

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getIPPort() {
        return IPPort;
    }

    public void setIPPort(String IPPort) {
        this.IPPort = IPPort;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getIPType() {
        return IPType;
    }

    public void setIPType(String IPType) {
        this.IPType = IPType;
    }

    public String getIPSpeed() {
        return IPSpeed;
    }

    public void setIPSpeed(String IPSpeed) {
        this.IPSpeed = IPSpeed;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    private String Address;//ip网址
}
