/**
 * @(#)AfterDownload4TreeContent.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月19日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import com.geccocrawler.gecco.annotation.GeccoClass;
import com.geccocrawler.gecco.downloader.AfterDownload;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.response.HttpResponse;

/**
 * @作者:maozf
 * @文件名:AfterDownload4TreeContent
 * @版本号:1.0
 * @创建日期:2016年6月19日
 * @描述:
 */
@GeccoClass(TreeContent.class)
public class AfterDownload4TreeContent implements AfterDownload {

	/* (non-Javadoc)
	 * @see com.geccocrawler.gecco.downloader.AfterDownload#process(com.geccocrawler.gecco.request.HttpRequest, com.geccocrawler.gecco.response.HttpResponse)
	 */
	@Override
	public void process(HttpRequest request, HttpResponse response) {
		

	}

}
