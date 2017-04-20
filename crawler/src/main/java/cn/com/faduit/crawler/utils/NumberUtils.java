/**
 * 
 */
package cn.com.faduit.crawler.utils;

/**
 * @Description:
 * @FileName :NumberUtils.java
 * @author MuQuan.Li
 * @Date:2016年4月7日
 * @Version:V1.0
 * 
 */
public class NumberUtils {
	/**
	 * 
	 * @param val
	 * @param defaultVal
	 * @return
	 */
	public static float getFloat(Object val,float defaultVal){
		if(val==null){
			return defaultVal;
		}
		try{
			return Float.parseFloat(val.toString());
		}catch(Exception ex){
			//System.out.println("tranfor);
		}
		return defaultVal;
	}
	
	public static int getInt(Object val,int defaultVal){
		if(val==null){
			return defaultVal;
		}
		try{
			return Integer.parseInt(val.toString());
		}catch(Exception ex){
			
		}
		return defaultVal;
	}
}
