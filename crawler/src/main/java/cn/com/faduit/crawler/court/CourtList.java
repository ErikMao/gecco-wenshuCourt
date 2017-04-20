/**
 * @(#)CourtList.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月16日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import java.util.List;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Ajax;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

/**
 * @作者:maozf
 * @文件名:CourtList
 * @版本号:1.0
 * @创建日期:2016年6月16日
 * @描述:
 */
@Gecco(downloader="htmlUnitDownloder",matchUrl="http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6",timeout=8000,pipelines={"consolePipeline","courtListPipeline"})
public class CourtList implements HtmlBean{
	
	
	/**
	 * @名称:serialVersionUID 
	 * @描述:TODO
	 */
	private static final long serialVersionUID = 1L;
	
	@Request
	private HttpRequest request;
	
	@HtmlField(cssPath=".dataItem")
	private List<CaseBrief> docList;
	
	
	
	public List<CaseBrief> getDocList() {
		return docList;
	}



	public void setDocList(List<CaseBrief> docList) {
		this.docList = docList;
	}



	public HttpRequest getRequest() {
		return request;
	}



	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	

	public static void main(String[] args) {
		final String StartURl = "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6";
		HttpGetRequest start = new HttpGetRequest(StartURl);
		//start.setCharset("UTF-8");
		
		
		GeccoEngine.create()
		.classpath("com.sunrise.crawlerdemo.crawler.court")
		.thread(1)
		.start(StartURl)		
		.interval(2000)
		.run();
	}
}
