/**
 * 
 */
package org.hippoproject.spider.transformer;

import org.hippoproject.spider.IParameterize;
import org.hippoproject.spider.ISpider;

/**
 * @author shixin
 *
 */
public interface ITransformer extends IParameterize {
	
	public String getBehaviorName();

	public void addField(TransformField field);
	
	public TransformField findField(String fieldName);
	
	public TransformField[] getFields();
	
	public Object getFieldValue(Object item, String fieldName, ISpider spider) throws Exception;
	
}
