/**
 * 
 */
package org.hippoproject.spider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shixin
 *
 */
public class ScriptParameter extends BaseParameter {

	public static final String TYPE = "script";
	
	private String value;
	
	public ScriptParameter(String name, String value) {
		super(name, TYPE);
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IParameter#getValue(org.hippoproject.spider.IParameterize)
	 */
	@Override
	public String getValue(IParameterize parameterize) {
		String val = DefaultSpiderManager.getInstance().resolveVariableValue(this.value, parameterize);
		
		Map<String, Object> bs = new HashMap<String, Object>();
		bs.put("spider", new SpiderToolbox(parameterize));
		
		Object variableValue = DefaultSpiderManager.getInstance().evalVariableValue(val, bs);
		
		return variableValue==null?"":variableValue.toString();
	}

}
