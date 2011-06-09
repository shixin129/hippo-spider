/**
 * 
 */
package org.hippoproject.spider;

/**
 * @author shixin
 *
 */
public interface IParameterize {

	public void setParameter(String name, String value);
	
	public void setParameter(IParameter parameter);
	
	public String getParameter(String name);
	
	public String getParameter(String name, String defaultValue);
	
	public void removeParameter(String name);
	
	public boolean hasParameter(String name);
	
	public String[] getParameterNames();
	
	public IParameterize getParent();
	
	public void setParent(IParameterize parent);
	
}
