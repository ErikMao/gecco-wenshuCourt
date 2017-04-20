/**
 * @(#)CaseBrief.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月16日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import com.geccocrawler.gecco.annotation.Href;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.spider.HtmlBean;

/**
 * @作者:maozf
 * @文件名:CaseBrief
 * @版本号:1.0
 * @创建日期:2016年6月16日
 * @描述:
 */
public class CaseBrief implements HtmlBean {

	/**
	 * @名称:serialVersionUID 
	 * @描述:TODO
	 */
	private static final long serialVersionUID = 1L;
	
	@Text
	@HtmlField(cssPath=" table > tbody > tr:nth-child(1) > td > div > a")
	private String title;
	
	@Href
	@HtmlField(cssPath=" table > tbody > tr:nth-child(1) > td > div > a")
	private String detailUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	
	
}
