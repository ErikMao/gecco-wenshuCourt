/**
 * @(#)ZuiMingDetailPipeline.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.zuiming;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.faduit.crawler.entity.ZuiMing;
import cn.com.faduit.crawler.service.ZuiMingService;
import cn.com.faduit.crawler.utils.StringUtils;

import com.geccocrawler.gecco.pipeline.Pipeline;

/**
 * @作者:maozf
 * @文件名:ZuiMingDetailPipeline
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
@Service("zuiMingDetailPipeline")
public class ZuiMingDetailPipeline implements Pipeline<ZuiMingDetail> {
	@Autowired
	private ZuiMingService zuiMingService;
	@Override
	public void process(ZuiMingDetail bean) {
		ZuiMing typeZuiMing = new ZuiMing();
		typeZuiMing.setParentBh(ZuiMing.PARENT_ROOT);
		String typeid = StringUtils.pad(bean.getTypeid(), 3, '0', false);
		typeZuiMing.setBh(typeid+"000");
		typeZuiMing.setMc(bean.getZuiming());	
		zuiMingService.add(typeZuiMing);
		List<String> subZuiMing = bean.getSubZuiMingList();
		int size = subZuiMing.size();
		for(int i=0;i< size;i++){			
			ZuiMing zuiMing = new ZuiMing();
			zuiMing.setBh(typeid+StringUtils.pad(String.valueOf(i), 3, '0', false));
			zuiMing.setParentBh(typeZuiMing.getBh());
			zuiMing.setMc(subZuiMing.get(i));
			zuiMingService.add(zuiMing);
		}
		
	}

}
