/**
 * @(#)ZuiMingList.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.zuiming;

import java.util.List;

import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.annotation.Attr;
import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.request.HttpGetRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

/**
 * @作者:maozf
 * @文件名:ZuiMingList
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
@Gecco(matchUrl="http://china.findlaw.cn/zuiming/",timeout=60000,pipelines={"consolePipeline","zuiMingListPipeline"})
public class ZuiMingList implements HtmlBean {

	/**
	 * @名称:serialVersionUID 
	 * @描述:TODO
	 */
	private static final long serialVersionUID = 1L;
	
	@Request
	private HttpRequest request;
	
	@Attr("typeid")
	@HtmlField(cssPath="div.sidebar.fl > ul > li > a")
	private List<String> zuimingType;
	/**
	 * @return the zuimingType
	 */
	public List<String> getZuimingType() {
		return zuimingType;
	}
	/**
	 * @param zuimingType the zuimingType to set
	 */
	public void setZuimingType(List<String> zuimingType) {
		this.zuimingType = zuimingType;
	}
	
	
	/**
	 * @return the request
	 */
	public HttpRequest getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpRequest request) {
		this.request = request;
	}
	public static void main(String[] args){
		HttpGetRequest start = new HttpGetRequest("http://china.findlaw.cn/zuiming/");
		GeccoEngine.create()
		.thread(1)
		.interval(2000)
		.start(start)
		.classpath("cn.com.faduit.crawler.zuiming")
		.run();
	}
	
	
}
