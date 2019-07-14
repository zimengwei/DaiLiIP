package com.cn.controller;

import com.cn.iputils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlerController {

    /**
     * 89免费IP代理
     *
     */
    @Autowired
    private EightyNineIPUtil eightyNineIPUtil;
    @RequestMapping("/startEightyNineIPUtil")
    public String startSixIPUtil() {
        //在新的线程中执行爬虫
        new Thread(new Runnable() {
            @Override
            public void run() {
                eightyNineIPUtil.doEightyNineIPUtil();
            }
        }).start();

        return "OK";
    }
    /**
     * 西刺IP代理 XiCiIpUtil
     */
    @Autowired
    private XiCiIpUtil xiCiIpUtil;
    @RequestMapping("/startXiCiIpUtil")
    public String startXiCiIpUtil() {
        //在新的线程中执行爬虫
        new Thread(new Runnable() {
            @Override
            public void run() {
                xiCiIpUtil.doXiCiIpUtil();
            }
        }).start();

        return "OK";
    }
    /**
     * 极速IP代理 doJiSuDaiLiIp
     */
    @Autowired
    private JiSuDaiLiIp jiSuDaiLiIp;
    @RequestMapping("/startJiSuDaiLiIp")
    public String startJiSuDaiLiIp() {
        //在新的线程中执行爬虫
        new Thread(new Runnable() {
            @Override
            public void run() {
                jiSuDaiLiIp.doJiSuDaiLiIp();
            }
        }).start();

        return "OK";
    }
    /**
     * 快代理IP代理 KuaiDaiLiIpUtil
     */
    @Autowired
    private KuaiDaiLiIpUtil kuaiDaiLiIpUtil;
    @RequestMapping("/startKuaiDaiLiIpUtil")
    public String startKuaiDaiLiIpUtil() {
        //在新的线程中执行爬虫
        new Thread(new Runnable() {
            @Override
            public void run() {
                kuaiDaiLiIpUtil.doKuaiDaiLiIpUtil();
            }
        }).start();

        return "OK";
    }
    /**
     * 西拉代理 XiLaDaiLiIp
     */
    @Autowired
    private XiLaDaiLiIp xiLaDaiLiIp;
    @RequestMapping("/startXiLaDaiLiIp")
    public String startXiLaDaiLiIp() {
        //在新的线程中执行爬虫
        new Thread(new Runnable() {
            @Override
            public void run() {
                xiLaDaiLiIp.doXiLaDaiLiIp();
            }
        }).start();

        return "OK";
    }
}
