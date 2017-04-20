/**
 * @(#)StartUrlUtils.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月18日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geccocrawler.gecco.downloader.DownloadException;
import com.geccocrawler.gecco.downloader.HttpClientDownloader;
import com.geccocrawler.gecco.request.HttpPostRequest;
import com.geccocrawler.gecco.response.HttpResponse;

/**
 * @作者:maozf
 * @文件名:StartUrlUtils
 * @版本号:1.0
 * @创建日期:2016年6月18日
 * @描述:
 */
public class StartUrlUtils {
	public static final String LISTCONTENT_URL = "http://wenshu.court.gov.cn/List/ListContent";
	/**
	 * 
	 * @更新时间:2016年6月18日
	 * @更新作者:maozf
	 * @param page
	 * @return
	 */
	public static HttpPostRequest getStartUrl(int page,String param){
		HttpPostRequest postStart = new HttpPostRequest(LISTCONTENT_URL);
		postStart.addField("Param", param);//一级案由:刑事案由,裁判年份:2016,法院地域:北京市,审判程序:一审,文书类型:判决书
		postStart.addField("Index", page+"");
		postStart.addField("Page", "20");
		postStart.addField("Order", "法院层级");
		postStart.addField("Direction", "asc");		
		postStart.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		//postStart.addHeader("Cookie", "CookieName=CookieValue; _gscu_1049835508=662573991290e419; Hm_lvt_9e03c161142422698f5b0d82bf699727=1466257402; CookieName=CookieValue; _gsref_2116842793=http://wenshu.court.gov.cn/; _gscu_2116842793=65899518v7oyze12; _gscs_2116842793=66486709lrpf5x88|pv:3; _gscbrs_2116842793=1; Hm_lvt_3f1a54c5a86d62407544d433f6418ef5=1466309147,1466416116,1466416136,1466486709; Hm_lpvt_3f1a54c5a86d62407544d433f6418ef5=1466486782");
		String t = URLEncoder.encode(param);
		postStart.addHeader("Referer", "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1+AJLX++"+t);
		postStart.addHeader("X-Requested-With", "XMLHttpRequest");
		//ls.add(start);
		return postStart;
	}
	public static HttpPostRequest getStartUrl(int page){
		HttpPostRequest postStart = new HttpPostRequest(LISTCONTENT_URL);
		postStart.addField("Param", "");//刑事案件
		postStart.addField("Index", page+"");
		postStart.addField("Page", "20");
		postStart.addField("Order", "法院层级");
		postStart.addField("Direction", "asc");		
		postStart.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		//postStart.addHeader("Cookie", "CookieName=CookieValue; ASP.NET_SessionId=z5jixaq4jtqui0liplp3zlud; Hm_lvt_3f1a54c5a86d62407544d433f6418ef5=1465899518,1466068047,1466173233; Hm_lpvt_3f1a54c5a86d62407544d433f6418ef5=1466203514; _gscu_2116842793=65899518v7oyze12; _gscs_2116842793=t66203514tjklcr16|pv:2; _gscbrs_2116842793=1; _gsref_2116842793=http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6");
		String t = "";
		postStart.addHeader("Referer", "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1+AJLX++"+t);
		postStart.addHeader("X-Requested-With", "XMLHttpRequest");
		//ls.add(start);
		return postStart;
	}
	public static HttpPostRequest wrapNextPage(HttpPostRequest postStart ,int page,String caseType){
		//HttpPostRequest postStart = new HttpPostRequest(LISTCONTENT_URL);
		//postStart.addField("Param", "案件类型:"+caseType);//刑事案件
		postStart.addField("Index", page+"");
		//postStart.addField("Page", "20");
		//postStart.addField("Order", "法院层级");
		//postStart.addField("Direction", "asc");		
		//postStart.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		//postStart.addHeader("Cookie", "CookieName=CookieValue; ASP.NET_SessionId=z5jixaq4jtqui0liplp3zlud; Hm_lvt_3f1a54c5a86d62407544d433f6418ef5=1465899518,1466068047,1466173233; Hm_lpvt_3f1a54c5a86d62407544d433f6418ef5=1466203514; _gscu_2116842793=65899518v7oyze12; _gscs_2116842793=t66203514tjklcr16|pv:2; _gscbrs_2116842793=1; _gsref_2116842793=http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E5%88%91%E4%BA%8B%E6%A1%88%E4%BB%B6");
		//String t = URLEncoder.encode("案件类型")+":"+URLEncoder.encode(caseType);
		//postStart.addHeader("Referer", "http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+1+AJLX++"+t);
		///\postStart.addHeader("X-Requested-With", "XMLHttpRequest");
		//ls.add(start);
		return postStart;
	}
	public static void main(String[] args) throws DownloadException{
		//initParam();
		HttpClientDownloader download = new HttpClientDownloader();
		String param = "一级案由:刑事案由";
		HttpPostRequest request = StartUrlUtils.getStartUrl(1,param);//一级案由:刑事案由,裁判年份:2016,法院地域:北京市,审判程序:一审,文书类型:判决书
		HttpResponse res = download.download(request, 30*1000);
		String content = res.getContent();
		System.out.println(content);
	}
	 
	private static void initParam(){
		 Map<String,List<String>> conditionMap = new LinkedHashMap<String,List<String>>();
		 try {
			String dic = FileUtils.readFileToString(new File("c:/crawlerDic.json"), "UTF-8");
			JSONArray dicJr = JSONArray.parseArray(dic);
			for(int i=0;i<dicJr.size();i++){
				JSONObject dicJo = dicJr.getJSONObject(i);
				String key = dicJo.getString("Key");
				
				List<String> items = new ArrayList<String>();
				JSONArray childJr = dicJo.getJSONArray("Child");
				if(childJr!=null && childJr.size()>0){
					for(int j=0;j<childJr.size();j++){
						JSONObject childJo = childJr.getJSONObject(j);
						String childKey = childJo.getString("Key");
						if(childKey!=null && !"".equals(childKey.trim())){
							items.add(key+":"+childKey);
						}
					}
				}
				conditionMap.put(key, items);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> result = new ArrayList<String>();
		Set<String> keys = conditionMap.keySet();
		
		List<String> pItemsList= new ArrayList<String>();
		for(String key:keys){
			if("关键词".equals(key) || "文书类型".equals(key)|| "法院层级".equals(key)||"审判程序".equals(key)){
				continue;
			}
			List<String> itemsList = conditionMap.get(key);
			System.out.println(key+":"+pItemsList.size()+"x"+itemsList.size());
			
			if(pItemsList.size()==0){
				pItemsList.addAll(itemsList);//第一个，不作笛卡尔积
			}else{
				//第二个开始要与前一次的结果做集合
				
				for(String tmp : pItemsList){
					
					for(String item:itemsList){
						String s = tmp+","+item;
						result.add(s);
						
					}
				}
				pItemsList.clear();
				pItemsList.addAll(result);
				result.clear();
			}
			
		}
		try {
			FileUtils.writeLines(new File("./params.txt"), pItemsList, "\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//http://wenshu.court.gov.cn/List/List?sorttype=1&conditions=searchWord+3+AJLX++%E6%A1%88%E4%BB%B6%E7%B1%BB%E5%9E%8B:%E8%A1%8C%E6%94%BF%E6%A1%88%E4%BB%B6
}
 