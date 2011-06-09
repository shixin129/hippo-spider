/**
 * 
 */
package org.hippoproject.spider.extracter;

import java.util.Map;

/**
 * @author shixin
 *
 */
public interface IContentCaptureRequest {

	/**
	 * 获得请求地址
	 * @return
	 */
	public String getUrl();
	
	/**
	 * 设置URL
	 * @param url
	 */
	public void setUrl(String url);
	
	public String getCharset();
	
	public void setCharset(String charset);
	
	/**
	 * 获得方法
	 * @return
	 */
	public String getMethod();
	
	/**
	 * 设置方法
	 * @param method
	 */
	public void setMethod(String method);
	
	/**
	 * 获得请求数据
	 * @return
	 */
	public Map<String, String> getDatas();
	
	/**
	 * 获得请求头文件
	 * @return
	 */
	public Map<String, String> getHeaders();
	
}
