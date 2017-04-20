/**
 * @(#)CourListPipeline.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月16日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import org.springframework.stereotype.Service;

import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.scheduler.SchedulerContext;

/**
 * @作者:maozf
 * @文件名:CourListPipeline
 * @版本号:1.0
 * @创建日期:2016年6月16日
 * @描述:
 */
@PipelineName("courtListPipeline")
@Service("courtListPipeline")
public class CourtListPipeline implements Pipeline<CourtList> {

	@Override
	public void process(CourtList bean) {
		HttpPostRequest postStart = new HttpPostRequest("http://wenshu.court.gov.cn/List/ListContent");
		postStart.addField("Param", "案件类型:刑事案件");
		postStart.addField("Index", "1");
		postStart.addField("Page", "20");
		postStart.addField("Order", "法院层级");
		postStart.addField("Direction", "asc");
		postStart.addHeader("Cookie", "_gscu_1049835508=65956550rze2t979; Hm_lvt_9e03c161142422698f5b0d82bf699727=1465956553,1466038375; Hm_lvt_3f1a54c5a86d62407544d433f6418ef5=1466038693,1466038722,1466038883,1466067665; Hm_lpvt_3f1a54c5a86d62407544d433f6418ef5=1466067901; _gscu_2116842793=6588766280sfw214; _gscbrs_2116842793=1");
		postStart.addHeader("Referer", bean.getRequest().getUrl());
		SchedulerContext.into(postStart);
		
	}

}
