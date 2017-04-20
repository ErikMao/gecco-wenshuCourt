/**
 * @(#)TreeContentPipeline.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月19日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import org.springframework.stereotype.Service;

import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;

/**
 * @作者:maozf
 * @文件名:TreeContentPipeline
 * @版本号:1.0
 * @创建日期:2016年6月19日
 * @描述:
 */
@Service("treeContentPipeline")
@PipelineName("treeContentPipeline")
public class TreeContentPipeline implements Pipeline<TreeContent> {

	@Override
	public void process(TreeContent bean) {
		System.out.println("treeContentPipeline");
		System.out.println(bean.getHtml());
		String jsonData = JsonUtils.clean(bean.getHtml());
		System.out.println(jsonData);
		
		
	}

}
