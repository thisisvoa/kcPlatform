package com.kcp.platform.core.web;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.kcp.platform.core.exception.BaseException;
import com.kcp.platform.core.exception.NotInterceptException;
import com.kcp.platform.core.exception.ResponseError;
import com.kcp.platform.core.exception.SystemException;
import com.kcp.platform.sys.constants.CommonConst;
import com.kcp.platform.util.AjaxJsonResponse;
import com.kcp.platform.util.JsonParser;

/**
 *
 *类描述：Multi Action 控制器的基类
 * 
 *@Version：1.0
 */
public class BaseMultiActionController extends MultiActionController {
	
	protected transient final Log log = LogFactory.getLog(getClass());
	
	@Autowired
    private ResourceBundleMessageSource messageSource;
	
	public BaseMultiActionController() {
		
    }
	
	/**
     * 获取传入参数的资源消息
     * @param key
     * @param args
     * @return
     */
    protected String getBundleMessage(String key,Object[] args){
        if(messageSource==null)
            throw new RuntimeException("MessageSourceAccessor should be injected before you use it!");
        return this.messageSource.getMessage(key,args,Locale.getDefault());

    }
    
	public void setMessageSource(ResourceBundleMessageSource messageSource)
    {
        this.messageSource = messageSource;
    }
	
	/**
	 * 输出XML字符串
	 * @param xml
	 * @param response
	 * @throws Exception
	 */
	protected void outputXML(String xml,HttpServletResponse response)throws Exception{
    	// 得到文件名
		OutputStream output = null;
    	// 文件类型
		response.setContentType("application/xml");
		response.setCharacterEncoding(System.getProperty("file.encoding"));
		
		try {
			output = response.getOutputStream();
			output.write(xml.getBytes());
			output.flush();
		} catch (Exception e) {
			throw new SystemException("输出XML数据出错", e);
		} finally {
			output.close();
		}
	}
	
    /**
     * 向客户端输出用户自定义成功消息
     *  
     * @param response
     * @param customMessage
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected void outputSuccessJsonResponseMessage(HttpServletResponse response,
            String customMessage) throws IOException
    {
        String responseContent = AjaxJsonResponse.generateSuccessfulOperationDoc(null,
                customMessage);
        this.flushResponse(response, responseContent);
    }

    /**
     * 向客户端输出操作成功的json格式的报文
     * @param response
     * @param customContent
     * @param customMsg
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    protected void outputSuccessJsonResponse(HttpServletResponse response,Map customContent, String customMsg)throws IOException
    {
        String responseContent=AjaxJsonResponse.generateSuccessfulOperationDoc(customContent,customMsg);
        this.flushResponse(response,responseContent);

    }


    /**
     * Method to flush a String as response.
     * @param response
     * @param responseContent
     * @throws IOException
     */
    protected void flushResponse(HttpServletResponse response,String responseContent)throws IOException{
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer=response.getWriter();
        writer.write(responseContent);
        writer.flush();
        writer.close();
    }


    /**
     * 输出XML文档
     *
     * @param response
     * @param xml
     * @throws Exception
     */
    public void outputXML(HttpServletResponse response, String xml)
        throws Exception {

        response.setContentType("text/xml; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(xml);
        out.flush();
        out.close();
    }
    
	/**
	 * 操作成功后的Javascript回调函数
	 * 
	 * @param param
	 * @return
	 */
	public String generateOperCallBackJS(String jsMethod,String param) {
		return "<script>"+jsMethod+"('" + param + "');</script>";
	}
	
	/**
	 * 异常拦截
	 * @param exception
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception, HttpServletRequest request, HttpServletResponse response)throws Exception {
		logger.error(request.getRequestURI() + " 请求失败", exception);
		ResponseError responseError = null;
		if(exception instanceof BaseException){
			if(exception instanceof NotInterceptException){
				throw exception;
			}else{
				responseError = new ResponseError(exception.getMessage());
			}
			
		}else{
			responseError = new ResponseError("系统未知异常！");
		}
		String requestURI = request.getRequestURI();
		responseError.setRequestURI(requestURI);
		//如果是ajax请求则直接输出异常信息
		if(requestURI.indexOf(".ajax")>-1 || "true".equals(request.getParameter(CommonConst.AJAX_REQUEST_FLAG))){
			response.setStatus(500);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JsonParser.convertObjectToJson(responseError));
			response.getWriter().flush();
			response.getWriter().close();
			return null;
		}
		return new ModelAndView("error/system_exception").addObject("responseError", responseError);
	}
}
