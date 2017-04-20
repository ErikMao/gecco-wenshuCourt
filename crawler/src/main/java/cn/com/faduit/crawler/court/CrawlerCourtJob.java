/**
 * 
 */
package cn.com.faduit.crawler.court;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.com.faduit.crawler.entity.CrawlerLog;
import cn.com.faduit.crawler.service.CrawlerLogService;
import cn.com.faduit.crawler.utils.DateUtils;

import com.alibaba.fastjson.JSONObject;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.pipeline.PipelineFactory;
import com.geccocrawler.gecco.request.HttpPostRequest;

/**
 * @author limq
 *
 */
@Service("crawlerCourtJob")
public class CrawlerCourtJob {
	private static final Logger logger = Logger.getLogger(CrawlerCourtJob.class);
	private static final String ScanPackage = "cn.com.faduit.crawler.court";
	private static final int Interval = 3000;
	
	@Autowired
	private PipelineFactory springPipelineFactory;
	
	@Autowired
	private CrawlerLogService crawlerLogService;
	
	private CrawlerLog log;
	private GeccoEngine engine;
	private boolean isStart;
	private int page = 1;
	@Value("#{fardobd.court_page}")
	private String courtPageFile;
	//private static final String PageInfoFilePath = "d:/limq/court_page.json";
	private static final String PageInfoFileEncode = "UTF-8";
	private static final int PageNum = 10;//每次抽取页数；//每个类别
	private String param;
	private String curDate;
	private String nextDate;
	private String caseType;
	public String getParam(){
		return this.param;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * 
	 * @更新时间:2016年6月18日
	 * @更新作者:maozf
	 */
	private void initPageInfo(){
		File f = new File(courtPageFile);
		
		if(f.exists()){
			try {				
				String s = FileUtils.readFileToString(f, PageInfoFileEncode);
				JSONObject pageJo = JSONObject.parseObject(s);
				this.page = pageJo.getIntValue("page");
				this.nextDate = pageJo.getString("curDate");
				if(nextDate==null || "".equals(nextDate)){
					nextDate = DateUtils.getCurrDate(DateUtils.HYPHEN_DISPLAY_DATE);
				}
				this.caseType = pageJo.getString("caseType");
				if(this.caseType == null || "".equals(caseType)){
					this.caseType = "刑事案件";
				}
				this.curDate = DateUtils.add(nextDate, DateUtils.HYPHEN_DISPLAY_DATE, -1, Calendar.DATE);
				this.param = "上传日期:"+this.curDate+" TO "+nextDate+",案件类型:"+this.caseType;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
		}
	}
	/**
	 * 
	 * @更新时间:2016年6月18日
	 * @更新作者:maozf
	 */
	public void savePageInfo(){
		JSONObject pageJo = new JSONObject();
		pageJo.put("page", this.page);
		pageJo.put("param", this.param);
		pageJo.put("curDate", this.curDate);
		pageJo.put("caseType", this.caseType);
		String s = pageJo.toJSONString();
		try {
			FileUtils.writeStringToFile(new File(courtPageFile), s, PageInfoFileEncode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void execute() {
		if(isStart){
			logger.warn("job had started!");
			return;
		}
		isStart = true;
		logger.info("start");
		//记录爬虫日志
		log = crawlerLogService.startCrawler();
		try {
			this.initPageInfo();
			System.out.println("start:page="+this.page+",param:"+this.param+"");
			HttpPostRequest start = StartUrlUtils.getStartUrl(1,this.param);
			logger.info("page="+page);
			engine = GeccoEngine.create()
				.classpath(ScanPackage)
				.pipelineFactory(springPipelineFactory)
				.interval(Interval)
				.thread(1)
				.retry(10)
				.start(start);
			engine.debug(false);//
			
			engine.run();
			System.out.println("启动任务完毕");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		logger.info("closeUntilComplete");
		engine.closeUnitlComplete();
		
		//爬虫结束后，记录日志状态及结束时间等
		crawlerLogService.finishCrawler(log);
		isStart = false;
		logger.info("end");
		this.page = 1;//
		this.savePageInfo();
	}
	/**
	 * 
	 * @return
	 */
	public CrawlerLog getCrawlerLog(){
		return this.log;
	}
}
