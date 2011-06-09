/**
 * 
 */
package org.hippoproject.spider;

import java.io.InputStream;
import java.util.Map;

import org.hippoproject.spider.extracter.IExtracter;
import org.hippoproject.spider.loader.ILoader;
import org.hippoproject.spider.transformer.ITransformer;

/**
 * @author shixin
 *
 */
public interface ISpiderManager {

	public ISpider getSpider(InputStream stream) throws Exception;
	
	public ISpider getSpider(String filePath) throws Exception;
	
	public ISpider getSpider(String charset, IExtracter extracter, ITransformer transformer, ILoader loader);
	
	public void registerExtracter(String behaviorName, Class clz);
	
	public void registerTransformer(String behaviorName, Class clz);
	
	public void registerLoader(String behaviorName, Class clz);
	
	public IExtracter findExtracter(String behaviorName);
	
	public ITransformer findTransformer(String behaviorName);
	
	public ILoader findLoader(String behaviorName);
	
	public IParameter createParameter(String name, String type, String value);
	
	public String resolveVariableValue(String value, String varFlag, IVariableResolver resolver);
	
	public String resolveVariableValue(String value, IParameterize parameterize);
	
	public Object evalVariableValue(String value, Map<String, Object> contextBeans);
	
}
