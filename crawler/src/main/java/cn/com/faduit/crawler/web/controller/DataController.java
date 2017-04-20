/**
 * @(#)DataController.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-10
 * 描　述：创建
 */

package cn.com.faduit.crawler.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.faduit.crawler.service.DataService;

/**
 * @作者:maozf
 * @文件名:DataController
 * @版本号:1.0
 * @创建日期:2016-7-10
 * @描述:
 */
@Controller
@RequestMapping("/data")
public class DataController {
	private static final Logger logger = Logger.getLogger(DataController.class);
	@Autowired
	private DataService dataService;
	@RequestMapping(params = "tran")
	public void start(HttpServletRequest request,HttpServletResponse res) throws IOException{
		logger.info("start tran data...");
		long t1 = System.currentTimeMillis();		
		dataService.tranDataToOracle();
		long elapsedTime = System.currentTimeMillis() - t1;
		res.getWriter().print("耗时:"+elapsedTime/1000 +"秒");
		logger.info("end");
	}
}
