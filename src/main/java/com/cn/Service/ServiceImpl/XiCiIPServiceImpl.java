package com.cn.Service.ServiceImpl;

import com.cn.Service.XiCiIPService;
import com.cn.dao.XiCiIpDao;
import com.cn.entity.XiCiIP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

import java.util.List;

public class XiCiIPServiceImpl implements XiCiIPService {
    @Autowired
    private XiCiIpDao xiCiIpDao;
    @Override
    public List<XiCiIP> findAll(XiCiIP xiCiIP) {
        Example example = Example.of(xiCiIP);
        List list = this.xiCiIpDao.findAll(example);
        return null;
    }

    @Override
    public void save(XiCiIP xiCiIP) {
        this.xiCiIpDao.save(xiCiIP);

    }
}
