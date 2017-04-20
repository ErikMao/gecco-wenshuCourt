/**
 * 
 */
package cn.com.faduit.crawler.web.core.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description:
 * @FileName :ApplicationContextUtil.java
 * @author MuQuan.Li
 * @Date:2016年4月14日
 * @Version:V1.0
 * 
 */
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;

	
	public void setApplicationContext(ApplicationContext context)
			 {
		this.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}

}
