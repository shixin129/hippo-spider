/**
 * 
 */
package org.hippoproject.spider.extracter;

/**
 * @author shixin
 *
 */
public interface ICaptureHandler {

	/**
	 * 处理请求对象
	 * @param request
	 */
	public void handleRequest(IContentCaptureRequest request);
	
	/**
	 * 处理相应对象
	 * @param response
	 */
	public void handleResponse(IContentCaptureResponse response);
	
}
