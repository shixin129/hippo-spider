/**
 * 
 */
package org.hippoproject.spider;

/**
 * @author shixin
 *
 */
public interface IParameter {

	public String getName();
	
	public String getType();
	
	public String getValue(IParameterize parameterize);
	
}
