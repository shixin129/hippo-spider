/**
 * 
 */
package org.hippoproject.spider.extracter;


/**
 * @author shixin
 *
 */
public interface IContentCapturer {
	/**
	 * 获得内容
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public IContentCaptureResponse capture(IContentCaptureRequest request) throws Exception;
	
	
	
}
