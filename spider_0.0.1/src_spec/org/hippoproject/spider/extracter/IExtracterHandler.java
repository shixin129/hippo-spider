/**
 * 
 */
package org.hippoproject.spider.extracter;

/**
 * @author shixin
 *
 */
public interface IExtracterHandler {

	public void handleException(Throwable tx);
	
	public void handleItem(Object item);
	
}
