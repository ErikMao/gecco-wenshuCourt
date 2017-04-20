/**
 * @(#)CrawlerJobController.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月17日
 * 描　述：创建
 */

package cn.com.faduit.crawler.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.faduit.crawler.court.CrawlerCourtJob;
import cn.com.faduit.crawler.zuiming.ZuiMingJob;

/**
 * @作者:maozf
 * @文件名:CrawlerJobController
 * @版本号:1.0
 * @创建日期:2016年6月17日
 * @描述:
 */
@Controller
@RequestMapping("/crawlerJob")
public class CrawlerJobController {
	private static final Logger logger = LoggerFactory.getLogger(CrawlerJobController.class);
	@Autowired
	private CrawlerCourtJob crawlerCourtJob;
	@Autowired
	private ZuiMingJob zuiMingJob;
	@RequestMapping(params = "start")
	public void start(HttpServletRequest request,HttpServletResponse res) throws IOException{
		logger.info("start");
		System.out.println("start");
		new Thread(){
			public void run(){
				crawlerCourtJob.execute();
			}
		}.start();
		logger.info("end");
		res.getWriter().print("start ok!!!!");
	}
	@RequestMapping(params = "zuiming")
	public void zuiming(HttpServletRequest request,HttpServletResponse res) throws IOException{
		logger.info("start");
		System.out.println("start");
		new Thread(){
			public void run(){
				zuiMingJob.execute();
			}
		}.start();
		logger.info("end");
		res.getWriter().print("zuiming start ok!!!!");
	}
}
