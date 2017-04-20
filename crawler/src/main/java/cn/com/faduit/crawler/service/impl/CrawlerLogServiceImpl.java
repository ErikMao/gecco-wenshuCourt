/**
 * 
 */
package cn.com.faduit.crawler.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.com.faduit.crawler.entity.CrawlerLog;
import cn.com.faduit.crawler.jpa.repository.CrawlerLogRespository;
import cn.com.faduit.crawler.service.CrawlerLogService;

/**
 * @Description:
 * @FileName :CrawlerLogServiceImpl.java
 * @author MuQuan.Li
 * @Date:2016年4月15日
 * @Version:V1.0
 * 
 */
@Service("crawlerLogService")
public class CrawlerLogServiceImpl implements CrawlerLogService {
	//@Autowired
	//private CrawlerLogMapper crawlerLogMapper;
	@Autowired
	private CrawlerLogRespository crawlerLogRespository;
	/* (non-Javadoc)
	 * @see com.sunrise.crawlerdemo.service.CrawlerLogService#startCrawler()
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public CrawlerLog startCrawler() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		CrawlerLog log = new CrawlerLog();
		String id = sdf.format(new Date());
		log.setId(id);
		log.setStatus(CrawlerLog.StatusOfStart);
		log.setCreateTime(new Timestamp(System.currentTimeMillis()));
		log.setFinishTime(null);
		crawlerLogRespository.save(log);
		//crawlerLogMapper.add(log);		
		return log;
	}

	/* (non-Javadoc)
	 * @see com.sunrise.crawlerdemo.service.CrawlerLogService#finishCrawler(com.sunrise.crawlerdemo.model.CrawlerLog)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void finishCrawler(CrawlerLog crawlerLog) {
		CrawlerLog newLog = crawlerLogRespository.getOne(crawlerLog.getId());
		newLog.setStatus(CrawlerLog.StatusOfFinish);
		newLog.setFinishTime(new Timestamp(System.currentTimeMillis()));
		crawlerLogRespository.save(newLog);
	}

	

}
