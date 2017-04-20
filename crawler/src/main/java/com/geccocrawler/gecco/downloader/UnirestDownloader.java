/**
 * 
 */
package com.geccocrawler.gecco.downloader;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;

import cn.com.faduit.crawler.court.JsonUtils;
import cn.com.faduit.crawler.court.StartUrlUtils;
import cn.com.faduit.crawler.entity.LawCase;
import cn.com.faduit.crawler.utils.DateUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.response.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.http.utils.ResponseUtils;
import com.mashape.unirest.request.HttpRequestWithBody;

/**
 * @Description:
 * @FileName :UnirestDownloader.java
 * @author MuQuan.Li
 * @Date:2016年5月11日
 * @Version:V1.0
 * 
 */
@com.geccocrawler.gecco.annotation.Downloader("unirestDownloder")
public class UnirestDownloader implements Downloader {
	private static Logger logger = Logger.getLogger(UnirestDownloader.class);

	public UnirestDownloader() {
		Unirest.setConcurrency(1000, 50);
	}

	private String getCharset(HttpRequest request, String contentType) {
		String charset = ResponseUtils.getCharsetFromContentType(contentType);
		if (charset == null) {
			charset = request.getCharset();
		}
		if (charset == null) {
			charset = "UTF-8";
		}
		return charset;
	}

	@Override
	public void shutdown() {
		try {
			Unirest.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		UnirestDownloader ud = new UnirestDownloader();
		final String url = "http://wenshu.court.gov.cn/List/ListContent";
		HttpPostRequest startUrl = StartUrlUtils.getStartUrl(1, "一级案由:刑事案由,裁判年份:2016,法院地域:北京市,审判程序:一审,文书类型:判决书");
		HttpResponse resp = ud.download(startUrl, 20000);
		String res = resp.getContent();
		System.out.println(res);
		String s1 = JsonUtils.clean(res);
		JSONArray jr = null;
		try{
			jr=JSONArray.parseArray(s1);		
		}catch(JSONException e){
			System.err.println(e);
			System.err.println(s1);
		}
		if(jr!=null && jr.size()>1){
			int count = jr.getJSONObject(0).getInteger("Count");
			int totalPage = count/20 +count%20==0?0:1;
			for(int i=1;i<jr.size();i++){
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
				//lawCase.setCrawlerTaskId(crawlerJob.getCrawlerLog().getId());
				
				logger.info(JSON.toJSONString(lawCase));
				/*try{
					//lawCaseService.addLawCase(lawCase);
					
					//加入详情页面地址
					if(docId!=null && !"".equals(docId)){
						HttpPostRequest getSummaryReq = (HttpPostRequest)request.subRequest("http://wenshu.court.gov.cn/Content/GetSummary");
						getSummaryReq.addField("docId", docId);
						SchedulerContext.into(getSummaryReq);
						logger.info("详情页面入队:"+docId);
					}
				}catch(Exception ex){
					//
					logger.error(ex.getMessage());
				}*/
				
			}
		}
	}

	@Override
	public HttpResponse download(HttpRequest request, int timeout) throws DownloadException {
		if (timeout > 0) {
			Unirest.setTimeouts(timeout, timeout);
		} else {
			Unirest.setTimeouts(3000, 3000);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("downloading..." + request.getUrl());
		}
		try {
			HttpHost proxy = Proxys.getProxy();
			if (proxy != null) {
				Unirest.setProxy(proxy);
			} else {
				Unirest.setProxy(null);
			}
			request.addHeader("User-Agent", UserAgent.getUserAgent(false));
			com.mashape.unirest.http.HttpResponse<String> response = null;
			if (request instanceof HttpPostRequest) {
				HttpPostRequest post = (HttpPostRequest) request;
				HttpRequestWithBody httpRequestWithBody = Unirest.post(post.getUrl());
				httpRequestWithBody.headers(post.getHeaders());
				httpRequestWithBody.fields(post.getFields());
				response = httpRequestWithBody.asString();
			} else {
				response = Unirest.get(request.getUrl()).headers(request.getHeaders()).asString();
			}
			String contentType = response.getHeaders().getFirst("Content-Type");
			HttpResponse resp = new HttpResponse();
			resp.setStatus(response.getStatus());
			resp.setRaw(response.getRawBody());
			resp.setContent(response.getBody());
			resp.setContentType(contentType);
			resp.setCharset(getCharset(request, contentType));
			return resp;
		} catch (UnirestException e) {
			throw new DownloadException(e);
		}
	}
}
