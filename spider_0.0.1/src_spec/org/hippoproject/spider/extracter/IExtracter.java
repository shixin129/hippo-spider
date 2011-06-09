/**
 * 
 */
package org.hippoproject.spider.extracter;

import org.hippoproject.spider.ILogger;
import org.hippoproject.spider.IParameterize;
import org.hippoproject.spider.ISpider;
import org.hippoproject.spider.SpiderContext;

/**
 * @author shixin
 *
 */
public interface IExtracter extends IParameterize {
	
	public String getBehaviorName();
	
	public void extract(IExtracterHandler handler, ISpider spider, SpiderContext context);
	
	public IContentCapturer getCapturer();
	
	public IContentCaptureRequest getCurrentRequest();
	
	public void setCaptureHandler(ICaptureHandler handler);
	
}
