/**
 * 
 */
package cn.com.faduit.crawler.service;

import cn.com.faduit.crawler.entity.CrawlerLog;

/**
 * @Description:
 * @FileName :CrawlerLogService.java
 * @author MuQuan.Li
 * @Date:2016年4月15日
 * @Version:V1.0
 * 
 */
public interface CrawlerLogService {
	/**
	 * 
	 * @return
	 */
	public CrawlerLog startCrawler();
	/**
	 * 
	 * @param crawlerLog
	 */
	public void finishCrawler(CrawlerLog crawlerLog);
	
}
