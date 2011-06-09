/**
 * 
 */
package org.hippoproject.spider;

/**
 * @author shixin
 *
 */
public interface ILogger {
	
	public static final int TYPE_ERROR = 2;
	public static final int TYPE_INFO = 1;
	public static final int TYPE_DEBUG = 0;

	public void log(int type, String log);
	
	public void info(String log);
	
	public void debug(String log);
	
	public void error(String log);
	
	public void error(String log, Throwable tx);
	
}
