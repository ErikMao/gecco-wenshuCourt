/**
 * @(#)ConsolePipeline.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.geccocrawler.gecco.spider.SpiderBean;

/**
 * @作者:maozf
 * @文件名:ConsolePipeline
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
@Service("consolePipeline")
public class ConsolePipeline implements Pipeline<SpiderBean> {

	@Override
	public void process(SpiderBean bean) {
		System.out.println(JSON.toJSONString(bean));
	}

}
