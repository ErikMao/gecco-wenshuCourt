/**
 * @(#)ZuiMingJob.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.zuiming;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.faduit.crawler.service.ZuiMingService;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.pipeline.PipelineFactory;
import com.geccocrawler.gecco.request.HttpGetRequest;

/**
 * @作者:maozf
 * @文件名:ZuiMingJob
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
@Service("zuiMingJob")
public class ZuiMingJob {
	private static final Logger logger = Logger.getLogger(ZuiMingJob.class);
	private static final String ScanPackage = "cn.com.faduit.crawler.zuiming";
	private static final int Interval = 2000;
	@Autowired
	private PipelineFactory springPipelineFactory;
	@Autowired
	private ZuiMingService zuiMingService; 
	private boolean isStart;
	private GeccoEngine engine;
	
	public void execute(){
		if(isStart){
			logger.warn("job had started!");
			return;
		}
		isStart = true;
		logger.info("start");
		zuiMingService.deleteAll();
		try{
			HttpGetRequest start = new HttpGetRequest("http://china.findlaw.cn/zuiming/");
			engine = GeccoEngine.create()
			.thread(1)
			.interval(Interval)
			.pipelineFactory(springPipelineFactory)
			.start(start)
			.retry(10)
			.classpath(ScanPackage);
			engine.run();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		logger.info("closeUntilComplete");
		engine.closeUnitlComplete();
		isStart = false;
	}
}
