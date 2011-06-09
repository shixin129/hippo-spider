/**
 * 
 */
package org.hippoproject.spider.loader;

import org.hippoproject.spider.IParameterize;
import org.hippoproject.spider.ISpider;
import org.hippoproject.spider.SpiderContext;
import org.hippoproject.spider.transformer.TransformField;

/**
 * @author shixin
 *
 */
public interface ILoader extends IParameterize {
	/**
	 * 返回行为名称
	 * @return
	 */
	public String getBehaviorName();
	/**
	 * 返回pre区域模板
	 * @return
	 */
	public String getTemplatePreSection();
	/**
	 * 设置pre区域的模板
	 * @param pre
	 */
	public void setTemplatePreSection(String pre);
	/**
	 * 返回post区域模板
	 * @return
	 */
	public String getTemplatePostSection();
	/**
	 * 设置post区域模板
	 * @param post
	 */
	public void setTemplatePostSection(String post);
	/**
	 * 返回body区域模板
	 * @return
	 */
	public String getTemplateBodySection();
	/**
	 * 设置body区域模板
	 * @param body
	 */
	public void setTemplateBodySection(String body);
	/**
	 * 数据项目迭代开始
	 * @param spider
	 * @param context
	 * @throws Exception
	 */
	public void before(ISpider spider, SpiderContext context) throws Exception;
	/**
	 * 数据项目迭代结束
	 * @param spider
	 * @param context
	 * @throws Exception
	 */
	public void after(ISpider spider, SpiderContext context) throws Exception;
	/**
	 * 数据项目迭代
	 * @param item
	 * @param fields
	 * @param fieldValues
	 * @param spider
	 * @param context
	 * @throws Exception
	 */
	public void item(Object item, TransformField[] fields, Object[] fieldValues, ISpider spider, SpiderContext context) throws Exception;
	
}
