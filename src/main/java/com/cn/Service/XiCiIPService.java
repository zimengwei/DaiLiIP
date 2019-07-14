package com.cn.Service;

import com.cn.entity.XiCiIP;

import java.util.List;

public interface XiCiIPService {
    //根据条件查询数据 西刺代理
    public List<XiCiIP> findAll(XiCiIP xiCiIP);
    //保存数据 西刺代理
    public void save(XiCiIP xiCiIP);
}
