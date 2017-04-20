/**
 * @(#)ZuiMingListPipeline.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.zuiming;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.scheduler.SchedulerContext;

/**
 * @作者:maozf
 * @文件名:ZuiMingListPipeline
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
@Service("zuiMingListPipeline")
public class ZuiMingListPipeline implements Pipeline<ZuiMingList> {
	private static final Logger logger = Logger.getLogger(ZuiMingListPipeline.class);
	@Override
	public void process(ZuiMingList bean) {
		HttpRequest request =bean.getRequest();
		for(String typeid:bean.getZuimingType()){
			String url = "http://china.findlaw.cn/zuiming/index.php?m=index&requestmode=async&a=getkw&typeid="+typeid;
			SchedulerContext.into(request.subRequest(url));
			logger.info("入队地址:"+url);
		}
		
		
	}

}
