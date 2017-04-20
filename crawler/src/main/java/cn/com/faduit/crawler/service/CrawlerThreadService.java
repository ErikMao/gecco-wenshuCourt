/**
 * @(#)CrawlerThreadService.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月20日
 * 描　述：创建
 */

package cn.com.faduit.crawler.service;

import org.springframework.stereotype.Service;

/**
 * @作者:maozf
 * @文件名:CrawlerThreadService
 * @版本号:1.0
 * @创建日期:2016年6月20日
 * @描述:
 */

public interface CrawlerThreadService {
	/**
	 * 开始爬取裁判文数据任务
	 * @更新时间:2016年6月20日
	 * @更新作者:maozf
	 */
	public void startCrawlerCourtData();
}
