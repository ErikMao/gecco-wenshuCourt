/**
 * 
 */
package cn.com.faduit.crawler.web.core.interceptors;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * @Description:
 * @FileName :MyWebBinding.java
 * @author MuQuan.Li
 * @Date:2016年4月14日
 * @Version:V1.0
 * 
 */
public class MyWebBinding implements WebBindingInitializer {

	/* (non-Javadoc)
	 * @see org.springframework.web.bind.support.WebBindingInitializer#initBinder(org.springframework.web.bind.WebDataBinder, org.springframework.web.context.request.WebRequest)
	 */
	@Override
	public void initBinder(WebDataBinder binder, WebRequest arg1) {
		// 1. 使用spring自带的CustomDateEditor
		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// binder.registerCustomEditor(Date.class, new
		// CustomDateEditor(dateFormat, true));
		//2. 自定义的PropertyEditorSupport
			binder.registerCustomEditor(Date.class, new DateConvertEditor());

	}

}
