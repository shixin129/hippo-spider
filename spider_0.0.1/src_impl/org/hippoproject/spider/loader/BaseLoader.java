/**
 * 
 */
package org.hippoproject.spider.loader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hippoproject.spider.BaseParameterize;
import org.hippoproject.spider.DefaultSpiderManager;
import org.hippoproject.spider.ISpider;
import org.hippoproject.spider.SpiderContext;
import org.hippoproject.spider.transformer.TransformField;

/**
 * @author shixin
 *
 */
public abstract class BaseLoader extends BaseParameterize implements ILoader {
	
	
	private String templatePreSection;
	private String templatePostSection;
	private String templateBodySection;
	public String getTemplatePreSection() {
		return DefaultSpiderManager.getInstance().resolveVariableValue(templatePreSection, this);
	}
	public void setTemplatePreSection(String templatePreSection) {
		this.templatePreSection = templatePreSection;
	}
	public String getTemplatePostSection() {
		return DefaultSpiderManager.getInstance().resolveVariableValue(templatePostSection, this);
	}
	public void setTemplatePostSection(String templatePostSection) {
		this.templatePostSection = templatePostSection;
	}
	public String getTemplateBodySection() {
		return DefaultSpiderManager.getInstance().resolveVariableValue(templateBodySection, this);
	}
	public void setTemplateBodySection(String templateBodySection) {
		this.templateBodySection = templateBodySection;
	}


	public static String buildTextTemplate(String body, TransformField[] fields, Object[] values){
		String REG_EXP = "@\\{([^\\}]+)\\}";
		Pattern REG_PATTERN = Pattern.compile(REG_EXP);
		
		StringBuffer sbf = new StringBuffer();
		Matcher templateMatcher = REG_PATTERN.matcher(body);
		while(templateMatcher.find()){
			String name = templateMatcher.group(1);
			Object val = getFieldValue(name,fields, values);
			templateMatcher.appendReplacement(sbf, val!=null?val.toString():"");
		}
		templateMatcher.appendTail(sbf);
		return sbf.toString();
	}
	
	protected static Object getFieldValue(String name,TransformField[] fields, Object[] values){
		for(int i=0,n=fields.length;i<n;i++){
			if(fields[i].getName().equalsIgnoreCase(name)){
				return values[i];
			}
		}
		return null;
	}
	@Override
	public void after(ISpider spider, SpiderContext context) throws Exception {
		
	}
	@Override
	public void before(ISpider spider, SpiderContext context) throws Exception {
		
	}
	@Override
	public String getBehaviorName() {
		return "";
	}

	
	
}
