/**
 * @(#)GetSummary.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月17日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

/**
 * @作者:maozf
 * @文件名:GetSummary
 * @版本号:1.0
 * @创建日期:2016年6月17日
 * @描述:
 */
@Gecco(matchUrl="http://wenshu.court.gov.cn/Content/GetSummary",timeout=60000,pipelines={"consolePipeline"})
public class GetSummary implements HtmlBean {

	/**
	 * @名称:serialVersionUID 
	 * @描述:TODO
	 */
	private static final long serialVersionUID = 1L;
	@Request
	private HttpRequest request;
	
	
	public HttpRequest getRequest() {
		return request;
	}
	

	public void setRequest(HttpRequest request) {
		this.request = request;
	}
	public static void main(String[] args){
		
		HttpPostRequest postStart = new HttpPostRequest("http://wenshu.court.gov.cn/Content/GetSummary");
		postStart.addField("docId", "8252121f-8260-4241-b707-018d52d151ca");
			
		
		GeccoEngine.create()
		.classpath("cn.com.faduit.crawler.court")
		.thread(1)
		.start(postStart)	
		.interval(2000)
		.run();
	}
}
