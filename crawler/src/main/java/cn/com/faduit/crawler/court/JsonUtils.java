/**
 * @(#)JsonUtils.java
 * 版权声明  巨龙软件工程有限公司, 版权所有 违者必究
 * 版本号: 
 * 修订记录:
 * 更改者：maozf
 * 时　间：2016年6月19日
 * 描　述：创建
 */

package cn.com.faduit.crawler.court;

/**
 * @作者:maozf
 * @文件名:JsonUtils
 * @版本号:1.0
 * @创建日期:2016年6月19日
 * @描述:
 */
public class JsonUtils {
	/**
	 * 去除斜杠
	 * \" 转成 "
	 * @更新时间:2016年6月19日
	 * @更新作者:maozf
	 * @param data
	 * @return
	 */
	public static String cleanSlash(String data){
		String newData = data.replaceAll("([{|,|:])\\s*\\\\\"", "$1\"")
		.replaceAll("\\\\\"\\s*([}|,|:])", "\"$1");
		return newData;
	}
	/**
	 * 去除前后引号
	 * @更新时间:2016年6月19日
	 * @更新作者:maozf
	 * @param data
	 * @return
	 */
	public static String cleanQuoat(String data){
		if(data.startsWith("\"") && data.endsWith("\"")){
			return data.substring(1, data.length()-1);
		}
		return data;
	}
	private static String removeUnicode(String jsJsonString){
		return jsJsonString.replaceAll("u0027", "\"");
	}
	/**
	 * 
	 * @更新时间:2016年6月19日
	 * @更新作者:maozf
	 * @param jsJsonString
	 * @return
	 */
	public static String clean(String jsJsonString){
		String s = removeUnicode(jsJsonString);
		s=cleanQuoat(s);
		s  = cleanSlash(s);
		return s;
	}
}
