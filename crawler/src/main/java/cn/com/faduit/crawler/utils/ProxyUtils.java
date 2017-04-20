/**
 * 
 */
package cn.com.faduit.crawler.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Description:
 * @FileName :ProxyUtils.java
 * @author MuQuan.Li
 * @Date:2016年4月13日
 * @Version:V1.0
 * 
 */
public class ProxyUtils {
	private static final Logger logger = Logger.getLogger(ProxyUtils.class);
	private static final String UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
	private static final String Referer = "http://www.kuaidaili.com/";
	private static final int TimeOut= 1000*30;
	public static void main(String[] args) throws IOException{
		Document doc = httpGet("http://www.kuaidaili.com/free/");
		String dochtml = doc.html();
		logger.info(dochtml);
		Elements eles = doc.select(".table-striped tbody tr");
		for(Element ele : eles){
			Elements tdEles = ele.select("td");
			String ip = tdEles.get(0).html().trim();
			String port = tdEles.get(1).html().trim();
			System.out.println(ip+":"+port);
			
		}
		//
		/*System.setProperty("http.maxRedirects", "50");  
        System.getProperties().setProperty("proxySet", "true");  
        // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以  
        String ip = "122.226.156.220:3128";  
        ip="58.96.183.136:8080";
        String[] hostAndPort = ip.split(":");
        String host = hostAndPort[0];
        String port = hostAndPort.length==2?hostAndPort[1]:"80";
        System.getProperties().setProperty("http.proxyHost", host);  
        System.getProperties().setProperty("http.proxyPort", port);  */
          
        //确定代理是否设置成功  
        //logger.info(getHtml("http://www.ip138.com/ip2city.asp")); 
	}
	public static Document httpGet(String url) throws IOException{
		Document doc = Jsoup.connect(url).userAgent(UserAgent).referrer(Referer).followRedirects(true).timeout(TimeOut).get();
		String dochtml = doc.html();
		int i =0;
		while(dochtml.contains("http://192")){
			doc = Jsoup.connect(url).userAgent(UserAgent).referrer(Referer).followRedirects(true).timeout(TimeOut).get();
			dochtml = doc.html();
			i++;
			System.out.println("retry times="+i);
			
		}
		return doc;
	}
}
