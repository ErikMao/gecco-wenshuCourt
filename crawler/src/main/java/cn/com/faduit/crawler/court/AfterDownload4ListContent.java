/**
 * @(#)AfterDownload.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月16日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.geccocrawler.gecco.annotation.GeccoClass;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.response.HttpResponse;
import com.geccocrawler.gecco.scheduler.SchedulerContext;

import cn.com.faduit.crawler.entity.LawCase;
import cn.com.faduit.crawler.service.LawCaseService;
import cn.com.faduit.crawler.utils.DateUtils;
import cn.com.faduit.crawler.web.core.util.ApplicationContextUtil;

/**
 * @作者:maozf
 * @文件名:AfterDownload
 * @版本号:1.0
 * @创建日期:2016年6月16日
 * @描述:
 */
@GeccoClass(ListContent.class)
public class AfterDownload4ListContent implements com.geccocrawler.gecco.downloader.AfterDownload {
	private static final Logger logger = Logger.getLogger(AfterDownload4ListContent.class);
	private static final AtomicLong TotalTimes = new AtomicLong(0);
	private static final AtomicLong ErrorTimes = new AtomicLong(0);
	
	/* (non-Javadoc)
	 * @see com.geccocrawler.gecco.downloader.AfterDownload#process(com.geccocrawler.gecco.request.HttpRequest, com.geccocrawler.gecco.response.HttpResponse)
	 */
	@Override
	public void process(HttpRequest request, HttpResponse response) {
		long total = TotalTimes.incrementAndGet();
		String content = response.getContent();
		if(content== null || content.contains("remind")){
			try {
				logger.error("remind sleep 30s");
				Thread.sleep(1000*60*30L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//logger.info(content);
		
		//String c1 = content.substring(1, content.length()-1);
		//System.out.println(c1);
		String s1 = JsonUtils.clean(content);
			
		JSONArray jr = null;
		try{
			jr=JSONArray.parseArray(s1);		
		}catch(JSONException e){
			logger.error(e);
			logger.error(content);
			logger.error(s1);
		}
		CrawlerCourtJob crawlerJob = ApplicationContextUtil.getContext().getBean("crawlerCourtJob", CrawlerCourtJob.class);
		if(jr!=null && jr.size()>1){
			LawCaseService lawCaseService = ApplicationContextUtil.getContext().getBean("lawCaseService", LawCaseService.class);
			int count = jr.getJSONObject(0).getInteger("Count");
			int totalPage = count/20 +(count%20==0?0:1);
			for(int i=1;i<jr.size();i++){
				handlerCase(request, jr, crawlerJob, lawCaseService, i);				
			}
			Map<String,Object> fields = ((HttpPostRequest)request).getFields();
			int curPage = Integer.parseInt(fields.get("Index").toString());
			logger.info("page="+curPage+",items size:"+jr.size()+",totalPage="+totalPage);
			logger.info("##"+total+"##"+request.getUrl());
			
			int nextPage = curPage+1;
			if( nextPage < totalPage && nextPage <100){				
				//String caseType = fields.get("Param").toString().replaceAll("案件类型:", "");		
				
				HttpPostRequest nextpageReq = StartUrlUtils.getStartUrl(nextPage, crawlerJob.getParam());//(HttpPostRequest)request.subRequest(StartUrlUtils.LISTCONTENT_URL);
				SchedulerContext.into(nextpageReq);
				crawlerJob.setPage(nextPage);
				crawlerJob.savePageInfo();
				logger.info("列表页面入队：index="+nextPage+",param="+fields.get("Param"));
			}
		}

	}

	/**
	 * @更新时间:2016年6月20日
	 * @更新作者:maozf
	 * @param request
	 * @param jr
	 * @param crawlerJob
	 * @param lawCaseService
	 * @param i
	 */
	private void handlerCase(HttpRequest request, JSONArray jr, CrawlerCourtJob crawlerJob,
			LawCaseService lawCaseService, int i) {
		JSONObject obj = (JSONObject)jr.get(i);
		LawCase lawCase = new LawCase();
		//文书ID
		String docId = obj.getString("文书ID");
		lawCase.setCaseId(docId);
		//审判程序
		lawCase.setCaseSlcx(obj.getString("审判程序"));
		//法院名称
		lawCase.setCourt(obj.getString("法院名称"));
		//裁判要旨段原文
		//裁判日期
		lawCase.setCaseDate(obj.getString("裁判日期").replaceAll("-", ""));
		//案件名称
		lawCase.setTitle(obj.getString("案件名称"));
		//案件类型
		lawCase.setCaseType(obj.getString("案件类型"));
		//案号
		lawCase.setCaseAh(obj.getString("案号"));
		//DocContent
		lawCase.setContent(obj.getString("DocContent"));
		 
		lawCase.setCreateTime(DateUtils.getCurrTime(DateUtils.DB_STORE_DATE));
		String crawlerUrl = "http://wenshu.court.gov.cn/content/content?DocID="+docId;
		lawCase.setCrawlerUrl(crawlerUrl);
		lawCase.setCrawlerTaskId(crawlerJob.getCrawlerLog().getId());
		
		logger.info(JSON.toJSONString(lawCase));
		try{
			lawCaseService.addLawCase(lawCase);
			
			//加入详情页面地址
			if(docId!=null && !"".equals(docId)){
				HttpPostRequest getSummaryReq = (HttpPostRequest)request.subRequest("http://wenshu.court.gov.cn/Content/GetSummary");
				getSummaryReq.getFields().clear();
				getSummaryReq.addField("docId", docId);
				getSummaryReq.addHeader("Referer", "http://wenshu.court.gov.cn/content/content?DocID="+docId);
				SchedulerContext.into(getSummaryReq);
				logger.info("详情页面入队:"+docId);
			}
		}catch(DuplicateKeyException e){
			logger.warn("案件已存在:docId="+docId);
		}catch(Exception ex){
			//
			logger.error(ex.getMessage());
		}
	}
	
	public static void main(String[] args){
		String ss = "法规名称:\u0027《中华人民共和国刑事诉讼法（2012年）》\u0027";
		System.out.println(ss.replaceAll("\\\u0027", "\""));
		
		String s ="[{\"Count\":\"3315955\"},{\"裁判要旨段原文\":\"本院经审查认为，宋皓通过个人借款、协调有业务关系及无业务关联的房地产开发公司借款及帮助申请贷款等多种途径为贵州新世纪集团房地产开发有限公司开发＆ｌｄｑｕｏ;世纪佳苑＆ｒｄｑｕｏ;、＆ｌｄｑｕｏ;世纪雅苑＆ｒｄｑｕｏ;两房地产项目提供启动资金、项目运营资金，并承担资金风险。原判认为宋皓没有实际出资，而是利用职务便利为请托人黄某某谋取利益，以合作投资名义收取＆ｌｄｑｕｏ;干股＆ｒｄｑｕｏ;的理由不充分。且宋皓的部分行为与其职务无关，原判未予考虑。综上，原判认定宋皓犯受贿罪的部分事实不清，证据不足。宋皓的申诉符合《中华人民共和国刑事诉讼法》第二百四十二条第（二）项、第（三）项规定的应当重新审判条件。据此，依照《中华人民共和国刑事诉讼法》第二百四十三条第二款、二百四十四条的规定，决定如下\",\"DocContent\":\"\u003cp\u003e\\n\u003ctitle\u003e\u003c\\/title\u003e\\n\u003c\\/p\u003e\\n \\n\\n\u003ctable border=\\\"1\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" xmlns=\\\"http://www.w3.org/1999/xhtml\\\"\u003e\\n\u003ctbody\u003e\\n\u003ctr\u003e\\n\u003ctd width=\\\"25\\\"\u003e\\n\u003cp\u003e\\n \u003c\\/p\u003e\\n\u003c\\/td\u003e\\n\u003c\\/tr\u003e\\n\u003c\\/tbody\u003e\\n\u003c\\/table\u003e\\n\\n中华人民共和国最高人民法院\\n再 审 决 定 书\\n（2012）刑监字第182-1号\\n被告人宋皓受贿一案，贵州省高级人民法院2010年4月2日作出（2010）黔高刑二终字第4号刑事判决，维持贵州省六盘水市中级人民法院（2009）黔六中刑三初字第32号刑事判决第一项，即：被告人宋皓犯受贿罪，判处无期徒刑，剥夺政治权利终身，并处没收个人财产人民币10万元；撤销贵州省六盘水市中级人民法院（2009）黔六中刑三初字第32号刑事判决第二项，改判上诉人宋皓受贿所得赃款赃物房屋及现金予以追缴，上缴国库。上述裁判发生法律效力后，宋皓以\u0026ldquo;其与黄某某是合作关系，借款与担保是两人之间约定的特殊合作方式，其参与了公司的日常经营管理活动；原判认定事实不清，适用法律错误\u0026rdquo;为由，向贵州省高级人民法院申诉，该院于2011年12月16日作出（2011）黔高调刑监字第25号通知驳回申诉。宋皓不服向本院申诉，本院于2013年3月14日作出（2012）刑监字第182号指令再审决定，指令贵州省高级人民法院再审本案。贵州省高级人民法院经再审于2013年12月25日作出（2013）黔高刑再终字第2号刑事裁定，驳回申诉，维持该院（2010）黔高刑二终字第4号刑事判决。宋皓不服，再次提出申诉。\\n本院经审查认为，宋皓通过个人借款、协调有业务关系及无业务关联的房地产开发公司借款及帮助申请贷款等多种途径为贵州新世纪集团房地产开发有限公司开发\u0026ldquo;世纪佳苑\u0026rdquo;、\u0026ldquo;世纪雅苑\u0026rdquo;两房地产项目提供启动资金、项目运营资金，并承担资金风险。原判认为宋皓没有实际出资，而是利用职务便利为请托人黄某某谋取利益，以合作投资名义收取\u0026ldquo;干股\u0026rdquo;的理由不充分。且宋皓的部分行为与其职务无关，原判未予考虑。综上，原判认定宋皓犯受贿罪的部分事实不清，证据不足。宋皓的申诉符合《中华人民共和国刑事诉讼法》第二百四十二条第（二）项、第（三）项规定的应当重新审判条件。据此，依照《中华人民共和国刑事诉讼法》第二百四十三条第二款、二百四十四条的规定，决定如下：\\n一、指令重庆市高级人民法院对本案进行再审。\\n二、再审期间，不停止原判决的执行。\\n二〇一四年十二月二十九日\",\"案件类型\":\"1\",\"裁判日期\":\"2014-12-29\",\"案件名称\":\"宋皓犯受贿罪刑事决定书\",\"文书ID\":\"8252121f-8260-4241-b707-018d52d151ca\",\"审判程序\":\"再审\",\"案号\":\"（2012）刑监字第182-1号\",\"法院名称\":\"最高人民法院\"},{\"裁判要旨段原文\":\"根据《中华人民共和国刑事诉讼法》第二十六条和《最高人民法院、最高人民检察院、公安部办理毒品犯罪案件适用法律若干问题的意见》第一条的规定，决定如下\",\"DocContent\":\"\u003cp\u003e\\n\u003ctitle\u003e\u003c\\/title\u003e\\n\u003c\\/p\u003e\\n \\n\\n\u003ctable border=\\\"1\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" xmlns=\\\"http://www.w3.org/1999/xhtml\\\"\u003e\\n\u003ctbody\u003e\\n\u003ctr\u003e\\n\u003ctd width=\\\"25\\\"\u003e\\n\u003cp\u003e\\n \u003c\\/p\u003e\\n\u003c\\/td\u003e\\n\u003c\\/tr\u003e\\n\u003c\\/tbody\u003e\\n\u003c\\/table\u003e\\n\\n中华人民共和国最高人民法院\\n指 定 管 辖 决 定 书\\n（2015）刑立他字第8号\\n根据《中华人民共和国刑事诉讼法》第二十六条和《最高人民法院、最高人民检察院、公安部办理毒品犯罪案件适用法律若干问题的意见》第一条的规定，决定如下：\\n指定山东省临沂市兰山区人民法院依照刑事第一审程序，对被告人邓玉玲非法持有毒品一案进行审判。\\n二〇一五年一月二十八日\",\"案件类型\":\"1\",\"裁判日期\":\"2015-01-28\",\"案件名称\":\"邓玉玲犯非法持有毒品罪刑事决定书\",\"文书ID\":\"afcea010-7f0b-4f89-9a0a-3023dfbefed2\",\"审判程序\":\"其他\",\"案号\":\"（2015）刑立他字第8号\",\"法院名称\":\"最高人民法院\"},{\"裁判要旨段原文\":\"本院经审查认为，原判认定王某某明知所借款项为公款、与国家工作人员共谋挪用公款的犯罪事实不清，证据不足，申诉人王某某的申诉符合《中华人民共和国刑事诉讼法》第二百四十二条第（二）项、第（三）项规定的应当重新审判条件。据此，依照《中华人民共和国刑事诉讼法》第二百四十三条第二款、二百四十四条的规定，决定如下\",\"DocContent\":\"\u003cp\u003e\\n\u003ctitle\u003e\u003c\\/title\u003e\\n\u003c\\/p\u003e\\n \\n\\n \\n中华人民共和国最高人民法院\\n再 审 决 定 书\\n（2015）刑监字第27号\\n被告人王某某挪用公款一案，山东省微山县人民法院于2011年11月22日作出（2011）微刑初字第190号刑事判决，被告人王某某犯挪用公款罪，判处有期徒刑二年，缓刑三年。宣判后，王某某提出上诉。山东省济宁市中级人民法院于2012年2月10日作出（2012）济刑终字第3号刑事裁定，驳回上诉，维持原判。王某某不服，提出申诉。山东省高级人民法院于2013年8月26日以（2013）鲁刑监字第38号通知，驳回申诉。王某某不服，以\u0026ldquo;原判认定事实不清，证据不足，没有参与共谋挪用公款，不构成犯罪\u0026rdquo;为由向本院提出申诉。\\n本院经审查认为，原判认定王某某明知所借款项为公款、与国家工作人员共谋挪用公款的犯罪事实不清，证据不足，申诉人王某某的申诉符合《中华人民共和国刑事诉讼法》第二百四十二条第（二）项、第（三）项规定的应当重新审判条件。据此，依照《中华人民共和国刑事诉讼法》第二百四十三条第二款、二百四十四条的规定，决定如下：\\n指令山东省高级人民法院对本案进行再审。\\n二〇一五年十月二十二日\",\"案件类型\":\"1\",\"裁判日期\":\"2015-10-22\",\"案件名称\":\"王守仁犯挪用公款罪刑事决定书\",\"文书ID\":\"029bb843-b458-4d1c-8928-fe80da403cfe\",\"审判程序\":\"再审\",\"案号\":\"（2015）刑监字第27号\",\"法院名称\":\"最高人民法院\"},{\"裁判要旨段原文\":\"本院经审查认为，申诉人方义的申诉符合《中华人民共和国刑事诉讼法》第二百四十二条规定的应当重新审判情形，决定如下\",\"DocContent\":\"\\n中华人民共和国最高人民法院\\n再 审 决 定 书\\n（2014）刑监字第11号\\n原审被告人方义职务侵占一案，新疆维吾尔自治区高级人民法院生产建设兵团分院于二〇一三年十一月十五日做出（2013）新兵监字第00022号驳回申诉通知，维持新疆生产建设兵团农十二师中级人民法院（2012）农十二刑终字第2号认定被告人方义犯职务侵占罪，判处有期徒刑十三年六个月，并处没收财产800000元，继续追缴赃款人民币9736886.70元的刑事裁定。裁定已经发生法律效力。方义遂向本院提出申诉。\\n本院经审查认为，申诉人方义的申诉符合《中华人民共和国刑事诉讼法》第二百四十二条规定的应当重新审判情形，决定如下：\\n一、指令新疆维吾尔自治区高级人民法院对本案进行再审。\\n二、再审期间，不停止原判决的执行。\\n二〇一四年七月二十一日\\n\",\"案件类型\":\"1\",\"裁判日期\":\"2014-07-21\",\"案件名称\":\"方职务侵占罪申诉刑事决定书\",\"文书ID\":\"5d9f09e0-d105-4a05-8634-31b811f2c9c9\",\"审判程序\":\"再审\",\"案号\":\"（2014）刑监字第11号\",\"法院名称\":\"最高人民法院\"},{\"裁判要旨段原文\":\"本院经审查认为，申诉人的申诉符合《中华人民共和国刑事诉讼法》第二百四十二条第（二）项规定的应当重新审判情形。据此，依照《中华人民共和国刑事诉讼法》第二百四十三条第二款的规定，决定如下\",\"DocContent\":\"\u003cp\u003e\\n\u0026nbsp;\u003c\\/p\u003e\\n\u003cp\u003e\\n\u0026nbsp;\u003c\\/p\u003e\\n\u003cp\u003e\\n\u0026nbsp;\u003c\\/p\u003e\\n\u003cp\u003e\\n\u0026nbsp;\u003c\\/p\u003e\\n\u003cp\u003e\\n\u003ctitle\u003e\u003c\\/title\u003e\\n\u003c\\/p\u003e\\n中华人民共和国最高人民法院\\n再 审 决 定 书\\n（2013）刑监字第59号\\n原审被告人王文杰合同诈骗一案，上海市第一中级人民法院于2008年11月20日以（2008）沪一中刑初字第122号刑事判决，认定被告人王文杰犯合同诈骗罪，判处无期徒刑，剥夺政治权利终身，并处没收个人全部财产；撤销青岛市中级人民法院（2006）青刑二初字第44号以虚开增值税专用发票罪判处被告人王文杰有期徒刑三年，缓刑五年，并处罚金人民币二十万元的缓刑部分，数罪并罚，决定执行无期徒刑，剥夺政治权利终身，并处没收个人全部财产。宣判后，王文杰提出上诉。上海市高级人民法院于2009年11月4日以（2008）沪高刑终字第190号刑事裁定，驳回上诉，维持原判。上述裁判发生法律效力后，王文杰的近亲属提出申诉。上海市高级人民法院于2011年10月30日以（2010）沪高刑监字第2号通知，驳回申诉。王文杰的近亲属遂向本院提出申诉。\\n本院经审查认为，申诉人的申诉符合《中华人民共和国刑事诉讼法》第二百四十二条第（二）项规定的应当重新审判情形。据此，依照《中华人民共和国刑事诉讼法》第二百四十三条第二款的规定，决定如下：\\n一、指令上海市高级人民法院另行组成合议庭对本案进行再审；\\n二、再审期间，不停止原判决（裁定）的执行。\\n二〇一三年十二月三十日\\n\",\"案件类型\":\"1\",\"裁判日期\":\"2013-12-30\",\"案件名称\":\"王某某合同诈骗指令再审决定书\",\"文书ID\":\"9aed51ab-f8b3-4f20-bf6e-06d8960c53d0\",\"审判程序\":\"再审审查与审判监督\",\"案号\":\"（2013）刑监字第59号\",\"法院名称\":\"最高人民法院\"},{\"裁判要旨段原文\":\"本院认为，被告人李良酒后无端滋事，持刀在公共场所行凶，致3人死亡、1人重伤、2人轻伤、3人轻微伤，其行为已构成故意杀人罪，且犯罪情节极其恶劣，后果严重，社会危害大，应依法惩处。第一审判决、第二审裁定认定的事实清楚，证据确实、充分，定罪准确，量刑适当。审判程序合法。依照《中华人民共和国刑事诉讼法》第二百三十五条、第二百三十九条和《最高人民法院关于适用〈中华人民共和国刑事诉讼法〉的解释》第三百五十条第（一）项的规定，裁定如下\",\"DocContent\":\"\\n中华人民共和国最高人民法院\\n刑 事 裁 定 书\\n被告人李良，男，俄罗斯族，1983年6月4日出生于新疆维吾尔自治区额敏县，高中文化，无业，住额敏县××镇××路××号。2012年1月6日被逮捕。现在押。\\n新疆维吾尔自治区乌鲁木齐市中级人民法院审理乌鲁木齐市人民检察院指控被告人李良犯故意杀人罪一案，于2012年12月11日以（2012）乌中刑一初字第106号刑事附带民事判决，认定被告人李良犯故意杀人罪，判处死刑，剥夺政治权利终身。宣判后，李良提出上诉。新疆维吾尔自治区高级人民法院经依法开庭审理，于2013年3月25日以（2013）新刑一终字第5号刑事附带民事裁定，驳回上诉，维持原判，并依法报请本院核准。本院依法组成合议庭，对本案进行了复核，依法讯问了被告人。现已复核终结。\\n经复核确认：2011年12月25日凌晨，被告人李良酒后到新疆维吾尔自治区乌鲁木齐市民主路193号哈乐淇淋吧城市慢摇吧3号店内娱乐。2时30分许，李良无端滋事，持随身携带的折叠刀，对被害人韩某某（男，殁年21岁）、马某（男，殁年20岁）、孙某某（男，殁年21岁）、邓某某（男，时年21岁）、许某某（男，时年23岁）、马某甲（男，时年19岁）、田某（男，时年19岁）、马某乙（男，时年27岁）、努尔某某·努尔某某（男，时年20岁）等人随意捅刺，致韩某某头臂干（动脉）破裂、右肺上叶贯通伤大失血死亡、马某外伤性失血性休克死亡、孙某某外伤性失血性休克死亡；致邓某某重伤和许某某、马某甲轻伤；致田某、马某乙、努尔某某·努尔某某轻微伤。\\n上述事实，有第一审、第二审开庭审理中经质证确认的被告人李良所用作案工具折叠刀，证人穆某某、张某某、田某某等的证言，被害人邓某某、许某某、马某甲、田某、马某乙、努尔某某·努尔某某的陈述，DNA鉴定意见、尸体鉴定意见、活体鉴定意见、精神病司法鉴定意见，现场勘验、检查、辨认等笔录和视频监控等证据证实。被告人李良亦供认。足以认定。\\n本院认为，被告人李良酒后无端滋事，持刀在公共场所行凶，致3人死亡、1人重伤、2人轻伤、3人轻微伤，其行为已构成故意杀人罪，且犯罪情节极其恶劣，后果严重，社会危害大，应依法惩处。第一审判决、第二审裁定认定的事实清楚，证据确实、充分，定罪准确，量刑适当。审判程序合法。依照《中华人民共和国刑事诉讼法》第二百三十五条、第二百三十九条和《最高人民法院关于适用〈中华人民共和国刑事诉讼法〉的解释》第三百五十条第（一）项的规定，裁定如下：\\n核准新疆维吾尔自治区高级人民法院（2013）新刑一终字第5号维持第一审以故意杀人罪判处被告人李良死刑，剥夺政治权利终身的刑事附带民事裁定。\\n本裁定自宣告之日起发生法律效力。\\n审　判　长　　张　杰\\n代理审判员　　王启全\\n代理审判员　　陈新军\\n二〇一三年六月二十四日\\n书　记　员　　杨　鸿\",\"案件类型\":\"1\",\"裁判日期\":\"2013-06-24\",\"案件名称\":\"李良故意杀人死刑复核案刑事裁定书\",\"文书ID\":\"f073d26d-b647-11e3-84e9-5cf3fc0c2c18\",\"审判程序\":\"其他\",\"案号\":\"无\",\"法院名称\":\"最高人民法院\"},{\"裁判要旨段原文\":\"本院认为，被告人崔昌贵、李占勇伙同他人贩卖、运输、制造“麻古”、氯胺酮，其行为已构成贩卖、运输、制造毒品罪。被告人杨林渠伙同他人贩卖、制造“麻古”，其行为已构成贩卖、制造毒品罪。被告人赵建东伙同他人贩卖、运输、制造氯胺酮，其行为已构成贩卖、运输、制造毒品罪。被告人刘文涛伙同他人贩卖、运输、制造“麻古”，伙同他人贩卖、运输氯胺酮，其行为已构成贩卖、运输、制造毒品罪。崔昌贵、李占勇、赵建东、刘文涛伙同他人制造毒品并进行贩卖、运输，杨林渠参与制造毒品并进行贩卖，五人在共同犯罪中均起主要作用，均系主犯，均应按照其所参与的全部犯罪处罚。崔昌贵参与制造、贩卖、运输“麻古”约137.097千克、氯胺酮约545.915千克；李占勇参与制造、贩卖、运输“麻古”约120.129千克、氯胺酮约351千克；杨林渠参与制造、贩卖“麻古”约105千克；赵建东参与制造、贩卖、运输氯胺酮约537.469千克；刘文涛参与制造、贩卖、运输“麻古”约36.929千克、氯胺酮约359千克，五被告人参与毒品犯罪的数量均特别巨大，且系长期从事源头性的毒品犯罪，大部分毒品已流入社会，社会危害极大，罪行极其严重。李占勇曾因犯罪被判刑，刑满释放后又犯罪，赵建东曾因犯罪被判处缓刑，缓刑考验期满后又犯罪，主观恶性深，人身危险性大。对五被告人均应依法惩处。第一审、第二审判决认定的事实清楚，证据确实、充分，定罪准确，量刑适当。审判程序合法。依照《中华人民共和国刑事诉讼法》第二百三十五条、第二百三十九条和《最高人民法院关于适用〈中华人民共和国刑事诉讼法〉的解释》第三百五十条第（一）项的规定，裁定如下\",\"DocContent\":\"\\n中华人民共和国最高人民法院\\n刑 事 裁 定 书\\n被告人崔昌贵，绰号“鬼子”，男，汉族，1970年3月18日出生，河南省洛阳市人，小学文化，无业，住洛阳市××区×××××小区×号楼×门×××号。2009年5月12日被逮捕。现在押。\\n被告人李占勇，男，汉族，1972年9月6日出生，河南省襄城县人，初中文化，无业，住河南省平顶山市××区××路××号院×号楼×号。1997年11月25日因犯敲诈勒索罪被判处有期徒刑二年，1999年9月11日刑满释放。2009年5月12日因本案被逮捕。现在押。\\n被告人杨林渠，绰号“阿文”，化名“赵明”、“王涛”、“邓耀清”，男，汉族，1979年7月10日出生，河南省襄城县人，初中文化，无业，住河南省许昌县×××镇××院。2009年6月19日被逮捕。现在押。\\n被告人赵建东，男，汉族，1974年4月12日出生，河南省襄城县人，初中文化，农民，住襄城县××镇××村×号×××。2006年6月20日因犯盗窃罪被判处有期徒刑一年，缓刑一年，并处罚金人民币五千元。2009年6月19日因本案被逮捕。现在押。\\n被告人刘文涛，绰号“肥仔”，男，汉族，1980年5月4日出生，河南省临颍县人，初中文化，无业，住临颍县×××镇×××村×××号。2009年5月12日被逮捕。现在押。\\n河南省漯河市中级人民法院审理漯河市人民检察院指控被告人崔昌贵、李占勇、赵建东、刘文涛犯贩卖、运输、制造毒品罪、被告人杨林渠犯贩卖、制造毒品罪一案，于2010年11月2日以（2010）漯刑三初字第06号刑事判决，认定被告人崔昌贵犯贩卖、运输、制造毒品罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产；被告人李占勇犯贩卖、运输、制造毒品罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产；被告人杨林渠犯贩卖、制造毒品罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产；被告人赵建东犯贩卖、运输、制造毒品罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产；被告人刘文涛犯贩卖、运输、制造毒品罪，判处死刑，剥夺政治权利终身，并处没收个人全部财产。宣判后，崔昌贵、李占勇、杨林渠、赵建东、刘文涛提出上诉。河南省高级人民法院经依法开庭审理，于2012年11月26日以(2011)豫法刑四终字第100号刑事判决，维持原审对被告人崔昌贵、李占勇、杨林渠、赵建东、刘文涛的判决，并依法报请本院核准。本院依法组成合议庭，对本案进行了复核，依法讯问了被告人，听取了辩护律师意见。现已复核终结。\\n经复核确认：\\n一、贩卖、运输、制造“麻古”（含甲基苯丙胺成分的片剂）的事实\\n1、2006年5月，被告人杨林渠、崔昌贵、李占勇伙同杨某某（在逃）等人共谋合伙制造“麻古”，由杨林渠提供制造“麻古”的主要原料甲基苯丙胺（冰毒），崔昌贵和杨某某购买辅料、租赁制毒地点以及负责加工制造，杨林渠按每粒一元向崔昌贵和杨某某支付加工费，制造的“麻古”由杨林渠、李占勇贩卖。之后，崔昌贵、杨某某等人在河南省洛阳市××区××路××街×号所租房屋内制造“麻古”约5万粒（每粒约0.1克，下同），由杨林渠、李占勇在广东省珠海市予以贩卖，其中李占勇贩卖约2.5万粒。\\n2、2006年8月至12月间，被告人杨林渠、崔昌贵、李占勇将生产“麻古”的场地从河南省洛阳市迁至由李占勇帮助租赁的广东省惠州市××区××镇××村××××厂一个楼顶的简易房，并将制毒工具从洛阳市运至该制毒场地。李占勇指使同案被告人严伟雄（已判刑）帮助杨林渠在广东省深圳市购买甲基苯丙胺11千克。杨林渠将该批甲基苯丙胺交给崔昌贵、杨某某制造出“麻古”100余万粒，由其与李占勇贩卖，其中李占勇贩卖约20余万粒。\\n3、2007年5月至6月间，被告人李占勇、刘文涛购买甲基苯丙胺，交由被告人崔昌贵和杨某某在广东省惠州市××区××镇××村××××厂的另一楼顶简易房内，利用之前的工具制造“麻古”10余万粒。李占勇、刘文涛等人将制成的部分“麻古”予以贩卖。\\n4、2008年8月至2009年4月间，被告人崔昌贵、刘文涛共谋合伙制造、贩卖“麻古”。刘文涛出资购买甲基苯丙胺约2？000余克（其中约700克甲基苯丙胺由崔昌贵通过被告人李占勇购买）。崔昌贵购买了咖啡因、香兰素等辅料，先后在河南省漯河市××区××路××号院×号楼×单元×楼×户所租房屋、×××苑×号楼×单元×楼×户所租房屋、××区××路×××村熊某某家制造“麻古”共计约20万粒。崔昌贵指使同案被告人于四福、李昌茂（均已判刑）等人将制成的部分“麻古”运到广东省珠海市交由刘文涛贩卖，其余部分由崔昌贵、同案被告人郑公社、翟进珂（均已判刑）等人在漯河市、洛阳市等地贩卖。案发后公安机关从上述制毒地点查获“麻古”成品、半成品及含有甲基苯丙胺的物质共2？097克、咖啡因4？045.9克、海洛因1克、麻黄素9.7克、氯胺酮446克以及香兰素等制毒辅料和搅拌机、压片机等制毒工具。\\n5、2007年6月至2008年11月，被告人崔昌贵多次通过同案被告人李昌茂（已判刑）从山西省购买制造“麻古”的原料咖啡因共计70余千克。\\n上述事实，有第一审、第二审开庭审理中经质证确认的从被告人崔昌贵的租住处查获的“麻古”成品、半成品、含有甲基苯丙胺的物质、咖啡因等毒品及制毒辅料、制毒工具等物的照片，银行业务凭证、证实被告人李占勇曾因犯罪被判刑的释放证明，证人李某某、杜某某、张某等的证言，毒品鉴定意见，现场勘验、检查、辨认笔录，同案被告人严伟雄、郑公社、于四福、李昌茂、梁连成、翟进珂、陈贵花的供述等证据证实。被告人崔昌贵、李占勇、刘文涛亦供认，被告人杨林渠亦曾供认。足以认定。\\n二、贩卖、运输、制造氯胺酮（俗称“K粉”）的事实\\n1、2008年12月，被告人崔昌贵、赵建东、李占勇、刘文涛共谋合伙制造氯胺酮，由崔昌贵纠集同案被告人郑公社、于四福、李昌茂以及梁连成（已判刑）参与制造，由赵建东雇请段某某、“军军”（均在逃）进行指导，氯胺酮制成后运至珠海市由李占勇、刘文涛贩卖。之后，崔昌贵指使梁连成租赁漯河市××路××立交桥旁一个废窑场作为制毒场地；郑公社、梁连成购买了脱水机等制毒工具；赵建东购买了乙酯、丙酮等制毒辅料。崔昌贵出资10万元，赵建东出资20万元，郑公社出资3.5万元，梁连成出资4万元，由赵建东、崔昌贵从同案被告人陈冬浩（已判刑）处以每箱4.5万元的价格购买了8箱制毒主料盐酸羟亚胺。其间李占勇购得6箱半盐酸羟亚胺指使他人运至漯河市交给崔昌贵，段某某亦提供了3箱盐酸羟亚胺。赵建东、郑公社、于四福、梁连成、李昌茂、段某某、“军军”等人共制造氯胺酮190余千克，其中段某某带走30余千克。崔昌贵、李占勇安排刘文涛、郑公社等人将160千克氯胺酮运至广东省中山市交给李占勇。李占勇安排赵某某（甲）（另案处理）等人在珠海市予以贩卖。2009年3月，李昌茂受崔昌贵的指使前往珠海市从刘文涛处将其中质量较次的约7千克氯胺酮运回重新加工。\\n2、2009年1月至2月，被告人李占勇出资90万元，由被告人崔昌贵、赵建东从陈冬浩处购买20箱盐酸羟亚胺用于制造氯胺酮，其中王某某（另案处理）运走6箱。崔昌贵、赵建东、郑公社、于四福、梁连成等人在漯河市××路××立交桥旁的废窑场内继续制造氯胺酮。后崔昌贵等人恐罪行败露，又转移至梁连成租赁的漯河市××县××路×段赵某某（乙）出租房内继续制造。同年2月上旬，崔昌贵等人将制成的160千克氯胺酮运至广东省中山市交给被告人刘文涛。在李占勇的指使下，刘文涛、赵某某（甲）等人将该批氯胺酮贩卖。\\n3、2009年3月初，被告人崔昌贵、赵建东伙同同案被告人郑公社、李昌茂、于四福、梁连成将制造氯胺酮的场地从赵某某（乙）的出租房转移至漯河市××区×××村公墓旁边赵某某（丙）的果园。崔昌贵和赵建东从同案被告人陈冬浩处购买3箱盐酸羟亚胺，由赵建东、郑公社等人在果园内制造氯胺酮24千克。崔昌贵、赵建东安排于四福和“老四”（在逃）乘长途汽车将该批氯胺酮运至广东省东莞市，由“老四”予以贩卖。于四福将贩卖所得的15万元赃款带回漯河交给崔昌贵。之后，崔昌贵和赵建东又从陈冬浩处购买4箱盐酸羟亚胺继续在果园制造氯胺酮，被公安机关抓获。\\n4、2008年11月，被告人崔昌贵指使于四福乘长途汽车将用被子包裹的8千克氯胺酮从河南省漯河市运至广东省珠海市交给被告人刘文涛。后因质量问题，崔昌贵又指使于四福前去珠海市从刘文涛处将该批氯胺酮运回。\\n5、2008年10月，被告人李占勇在河南省平顶山市分两次向他人购买了12千克和19千克氯胺酮，装入旅行箱内，伙同同案被告人李瑛（已判刑）、赵某某（甲）、耿某某（在逃）驾车从平顶山市运输至珠海市，存放于被告人刘文涛为李占勇所租的房屋内。后李占勇、刘文涛将该批氯胺酮在珠海市予以贩卖。\\n案发后，公安机关从制毒场所漯河市××县××路×段赵某某（乙）出租房内查获正在晾晒的氯胺酮13.315千克及脱水机等制毒工具；从漯河市××区××村公墓旁边赵某某（丙）的果园内查获含有氯胺酮的物质150.154千克及脱水机、活性炭、氨水等制毒工具和原料；从被告人刘文涛在珠海市为被告人李占勇租赁的用于存放毒品的三处出租房内查获“麻古”4？019.33克、含有甲基苯丙胺成分的物质6？929.44克、氯胺酮10.57424千克、咖啡因1.31千克和麻黄素54.28克，以及电子秤、筛子、空塑料封口袋等贩毒工具。公安人员在抓获李占勇、刘文涛时，从二人随身携带的提包内查获甲基苯丙胺和“麻古”共216.43克。\\n上述事实，有第一审、第二审开庭审理中经质证确认的查获的氯胺酮、甲基苯丙胺、“麻古”、含有氯胺酮的物质以及制毒辅料、制毒工具、贩毒工具等物的照片，银行业务凭证、证实被告人赵建东曾因犯罪被判刑的刑事判决书，证人马某某、赵某某（乙）、赵某某（丙）等的证言，毒品鉴定意见，现场勘验、检查、辨认笔录，同案被告人郑公社、于四福、李昌茂、梁连成、陈冬浩、李旭照、陈贵花、李瑛的供述等证据证实。被告人崔昌贵、李占勇、赵建东、刘文涛亦供认。足以认定。\\n本院认为，被告人崔昌贵、李占勇伙同他人贩卖、运输、制造“麻古”、氯胺酮，其行为已构成贩卖、运输、制造毒品罪。被告人杨林渠伙同他人贩卖、制造“麻古”，其行为已构成贩卖、制造毒品罪。被告人赵建东伙同他人贩卖、运输、制造氯胺酮，其行为已构成贩卖、运输、制造毒品罪。被告人刘文涛伙同他人贩卖、运输、制造“麻古”，伙同他人贩卖、运输氯胺酮，其行为已构成贩卖、运输、制造毒品罪。崔昌贵、李占勇、赵建东、刘文涛伙同他人制造毒品并进行贩卖、运输，杨林渠参与制造毒品并进行贩卖，五人在共同犯罪中均起主要作用，均系主犯，均应按照其所参与的全部犯罪处罚。崔昌贵参与制造、贩卖、运输“麻古”约137.097千克、氯胺酮约545.915千克；李占勇参与制造、贩卖、运输“麻古”约120.129千克、氯胺酮约351千克；杨林渠参与制造、贩卖“麻古”约105千克；赵建东参与制造、贩卖、运输氯胺酮约537.469千克；刘文涛参与制造、贩卖、运输“麻古”约36.929千克、氯胺酮约359千克，五被告人参与毒品犯罪的数量均特别巨大，且系长期从事源头性的毒品犯罪，大部分毒品已流入社会，社会危害极大，罪行极其严重。李占勇曾因犯罪被判刑，刑满释放后又犯罪，赵建东曾因犯罪被判处缓刑，缓刑考验期满后又犯罪，主观恶性深，人身危险性大。对五被告人均应依法惩处。第一审、第二审判决认定的事实清楚，证据确实、充分，定罪准确，量刑适当。审判程序合法。依照《中华人民共和国刑事诉讼法》第二百三十五条、第二百三十九条和《最高人民法院关于适用〈中华人民共和国刑事诉讼法〉的解释》第三百五十条第（一）项的规定，裁定如下：\\n核准河南省高级人民法院(2011)豫法刑四终字第100号维持第一审对被告人崔昌贵以贩卖、运输、制造毒品罪判处死刑，剥夺政治权利终身，并处没收个人全部财产；对被告人李占勇以贩卖、运输、制造毒品罪判处死刑，剥夺政治权利终身，并处没收个人全部财产；对被告人杨林渠以贩卖、制造毒品罪判处死刑，剥夺政治权利终身，并处没收个人全部财产；对被告人赵建东以贩卖、运输、制造毒品罪判处死刑，剥夺政治权利终身，并处没收个人全部财产；对被告人刘文涛以贩卖、运输、制造毒品罪判处死刑，剥夺政治权利终身，并处没收个人全部财产的刑事判决。\\n本裁定自宣告之日起发生法律效力。\\n审　判　长　　王培中\\n代理审判员　　翁彤彦\\n代理审判员　　毛　洁\\n二〇一三年六月二十四日\\n书　记　员　　陈琳琳\",\"案件