/**
 * @(#)ZuiMingService.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.service;

import cn.com.faduit.crawler.entity.ZuiMing;

/**
 * @作者:maozf
 * @文件名:ZuiMingService
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
public interface ZuiMingService {
	
	public void add(ZuiMing zuiMing);
	/**
	 * 爬取前先清理数据
	 * @更新时间:2016-7-8
	 * @更新作者:maozf
	 */
	public void deleteAll();
}
