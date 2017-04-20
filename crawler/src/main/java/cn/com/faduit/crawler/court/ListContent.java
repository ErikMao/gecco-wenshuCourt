/**
 * @(#)ListContent.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月16日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.Html;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

/**
 * @作者:maozf
 * @文件名:ListContent
 * @版本号:1.0
 * @创建日期:2016年6月16日
 * @描述:
 */
@Gecco(matchUrl="http://wenshu.court.gov.cn/List/ListContent",timeout=60000,pipelines={"consolePipeline"})
public class ListContent implements HtmlBean {
	
	/**
	 * @名称:serialVersionUID 
	 * @描述:TODO
	 */
	private static final long serialVersionUID = 1L;

	@Request
	private HttpRequest request;
	@Html
	private String html;
	
	
	public String getHtml() {
		return html;
	}


	public void setHtml(String html) {
		this.html = html;
	}


	public HttpRequest getRequest() {
		return request;
	}
	

	public void setRequest(HttpRequest request) {
		this.request = request;
	}


	public static void main(String[] args) {
		//final String StartURl = "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6";
		//HttpGetRequest start = new HttpGetRequest(StartURl);
		
			HttpPostRequest postStart = new HttpPostRequest("http://wenshu.court.gov.cn/List/ListContent");
			postStart.addField("Param", "裁判年份:2013");//一级案由:刑事案由,裁判年份:2016,法院地域:北京市,审判程序:一审,文书类型:判决书
			postStart.addField("Index", "1");
			postStart.addField("Page", "20");
			postStart.addField("Order", "法院层级");
			postStart.addField("Direction", "asc");			
			//ls.add(start);
			
		
		GeccoEngine.create()
		.classpath("cn.com.faduit.crawler.court")
		.thread(1)
		.start(postStart)	
		.interval(2000)
		.run();
	}
}
