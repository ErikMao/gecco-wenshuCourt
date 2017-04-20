/**
 * @(#)DataServiceImpl.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-10
 * 描　述：创建
 */

package cn.com.faduit.crawler.service.impl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.com.faduit.crawler.entity.CrawlerLog;
import cn.com.faduit.crawler.entity.LawCase;
import cn.com.faduit.crawler.jpa.repository.CrawlerLogRespository;
import cn.com.faduit.crawler.jpa.repository.LawCaseRespository;
import cn.com.faduit.crawler.service.DataService;

/**
 * @作者:maozf
 * @文件名:DataServiceImpl
 * @版本号:1.0
 * @创建日期:2016-7-10
 * @描述:
 */
@Service("dataService")
public class DataServiceImpl implements DataService {
	private static final Logger logger = Logger.getLogger(DataServiceImpl.class);
	@Autowired
	private CrawlerLogRespository crawlerLogRespository;
	@Autowired
	private LawCaseRespository lawCaseRespository;
	@Autowired
	private JdbcTemplate oracleJdbcTemplate;
	
	private static final String INSERT_SQL = "INSERT INTO LAW_CASE (CASE_ID,TITLE,CONTENT,PUB_DATE,COURT,CASE_AH,CASE_TYPE,CAUSE_ACTION,CASE_DATE,CASE_DSR,CASE_SLCX,CREATE_TIME,CRAWLER_URL,CRAWLER_TASK_ID) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	/* (non-Javadoc)
	 * @see cn.com.faduit.crawler.service.DataService#tranDataToOracle()
	 */
	@Override
	public void tranDataToOracle() {
		List<CrawlerLog> logs = crawlerLogRespository.findByStatus(CrawlerLog.StatusOfFinish);
		List<String> taskIdList = lawCaseRespository.findDistinctCrawlerTaskIdList();
		long totalCount = lawCaseRespository.count();
		int count = 0;
		int error = 0;
		//for(CrawlerLog log : logs){
		//	String crawlerTaskId = log.getId();
		for(String crawlerTaskId : taskIdList){
			List<LawCase> lawCaseList = lawCaseRespository.findByCrawlerTaskId(crawlerTaskId);
			for(LawCase lc:lawCaseList){
				Object[] params = new Object[]{
						lc.getCaseId(),
						lc.getTitle(),
						lc.getContent(),
						lc.getPubDate(),
						lc.getCourt(),
						lc.getCaseAh(),
						lc.getCaseType(),
						lc.getCauseAction(),
						lc.getCaseDate(),
						lc.getCaseDsr(),
						lc.getCaseSlcx(),
						lc.getCreateTime(),
						lc.getCrawlerUrl(),
						lc.getCrawlerTaskId()
				};
				try{
					oracleJdbcTemplate.update(INSERT_SQL,params);
					count++;
				}catch(Exception ex){
					logger.error(ex);
					error++;
				}
				logger.info("【"+count+"/"+totalCount+",error="+error+"】"+lc.getCaseId());
			}
		}

	}

}
