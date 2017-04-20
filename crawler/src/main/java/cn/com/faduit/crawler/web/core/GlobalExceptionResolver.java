/**
 * 
 */
package cn.com.faduit.crawler.web.core;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.com.faduit.crawler.utils.ConvertUtils;

/**
 * @Description:
 * @FileName :GlobalExceptionResolver.java
 * @author MuQuan.Li
 * @Date:2016年4月14日
 * @Version:V1.0
 * 
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
	private static final Logger logger = Logger.getLogger(GlobalExceptionResolver.class);
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.HandlerExceptionResolver#resolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,
			Exception ex) {
		boolean isajax = isAjax(request,response);
		Throwable deepestException = deepestException(ex);
		logger.error("全局处理异常捕获:", ex);
		return null;
	}
	/**
	 * 判断当前请求是否为异步请求.
	 */
	private boolean isAjax(HttpServletRequest request, HttpServletResponse response){
		return ConvertUtils.isNotEmpty(request.getHeader("X-Requested-With"));
	}
	/**
	 * 获取最原始的异常出处，即最初抛出异常的地方
	 */
    private Throwable deepestException(Throwable e){
        Throwable tmp = e;
        int breakPoint = 0;
        while(tmp.getCause()!=null){
            if(tmp.equals(tmp.getCause())){
                break;
            }
            tmp=tmp.getCause();
            breakPoint++;
            if(breakPoint>1000){
                break;
            }
        } 
        return tmp;
    }
    
	
}
