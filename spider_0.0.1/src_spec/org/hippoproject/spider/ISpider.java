/**
 * 
 */
package org.hippoproject.spider;

import org.hippoproject.spider.extracter.IExtracter;
import org.hippoproject.spider.loader.ILoader;
import org.hippoproject.spider.transformer.ITransformer;

/**
 * @author shixin
 *
 */
public interface ISpider extends IParameterize {
	
	public static final String CHARSET_AUTO = "AUTO";

	/**
	 * 获得指定的字符编码
	 * @return
	 */
	public String getCharset();
	
	/**
	 * 获得抽取器
	 * @return
	 */
	public IExtracter getExtracter();
	
	/**
	 * 获得加载器
	 * @return
	 */
	public ILoader getLoader();
	
	/**
	 * 获得转换器
	 * @return
	 */
	public ITransformer getTransformer();
	
	/**
	 * 执行
	 * @throws Exception
	 */
	public void run(SpiderContext context) throws Exception;
	
}
