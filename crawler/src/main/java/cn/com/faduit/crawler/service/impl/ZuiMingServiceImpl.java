/**
 * @(#)ZuiMingServiceImpl.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.faduit.crawler.entity.ZuiMing;
import cn.com.faduit.crawler.jpa.repository.ZuiMingRespository;
import cn.com.faduit.crawler.service.ZuiMingService;

/**
 * @作者:maozf
 * @文件名:ZuiMingServiceImpl
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
@Service("zuMingService")
public class ZuiMingServiceImpl implements ZuiMingService {
	@Autowired
	private ZuiMingRespository zuiMingRespository;
	/* (non-Javadoc)
	 * @see cn.com.faduit.crawler.service.ZuiMingService#add(cn.com.faduit.crawler.entity.ZuiMing)
	 */
	@Override
	public void add(ZuiMing zuiMing) {
		//
		zuiMingRespository.save(zuiMing);

	}
	@Override	
	public void deleteAll() {
		zuiMingRespository.deleteAll();
		
	}

}
