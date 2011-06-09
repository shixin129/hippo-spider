/**
 * 
 */
package org.hippoproject.spider;

/**
 * @author shixin
 *
 */
public class SimpleParameter extends BaseParameter {
	
	public static final String TYPE = "simple";
	
	private String value;
	
	

	public SimpleParameter(String name, String value) {
		super(name, TYPE);
		this.value = value;
	}



	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IParameter#getValue()
	 */
	@Override
	public String getValue(IParameterize parameterize) {
		return DefaultSpiderManager.getInstance().resolveVariableValue(this.value, parameterize);
	}

}
