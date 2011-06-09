/**
 * 
 */
package org.hippoproject.spider;

/**
 * @author shixin
 *
 */
public abstract class BaseParameter implements IParameter{

	private String name;
	private String type;
	
	public BaseParameter(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getType() {
		return this.type;
	}

	
	
}
