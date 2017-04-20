/**
 * @(#)ZuiMingDetail.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016-7-8
 * 描　述：创建
 */

package cn.com.faduit.crawler.zuiming;

import java.util.List;

import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.annotation.RequestParameter;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;

/**
 * @作者:maozf
 * @文件名:ZuiMingDetail
 * @版本号:1.0
 * @创建日期:2016-7-8
 * @描述:
 */
@Gecco(matchUrl="http://china.findlaw.cn/zuiming/index.php?m=index&requestmode=async&a=getkw&typeid={typeid}",timeout=60000,pipelines={"consolePipeline","zuiMingDetailPipeline"})
public class ZuiMingDetail implements HtmlBean {
	@Request
	private HttpRequest request;
	
	@RequestParameter
	private String typeid;
	
	@Text
	@HtmlField(cssPath="dt > span")
	private String zuiming;
	
	@Text
	@HtmlField(cssPath="dd > a")
	private List<String> subZuiMingList;

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

	/**
	 * @return the typeid
	 */
	public String getTypeid() {
		return typeid;
	}

	/**
	 * @param typeid the typeid to set
	 */
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}

	/**
	 * @return the zuiming
	 */
	public String getZuiming() {
		return zuiming;
	}

	/**
	 * @param zuiming the zuiming to set
	 */
	public void setZuiming(String zuiming) {
		this.zuiming = zuiming;
	}

	/**
	 * @return the subZuiMingList
	 */
	public List<String> getSubZuiMingList() {
		return subZuiMingList;
	}

	/**
	 * @param subZuiMingList the subZuiMingList to set
	 */
	public void setSubZuiMingList(List<String> subZuiMingList) {
		this.subZuiMingList = subZuiMingList;
	}
	
}
