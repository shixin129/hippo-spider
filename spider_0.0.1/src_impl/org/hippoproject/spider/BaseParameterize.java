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
public class BaseParameterize implements IParameterize {
	
	private Map<String, IParameter> params = new HashMap<String, IParameter>();
	private IParameterize parent;

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IParameterize#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {
		IParameter parameter = this.params.get(name);
		if(parameter!=null){
			return parameter.getValue(this);
		}
		if(this.getParent()!=null){
			return this.getParent().getParameter(name);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IParameterize#getParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public String getParameter(String name, String defaultValue) {
		IParameter parameter = this.params.get(name);
		if(parameter!=null){
			return parameter.getValue(this);
		}
		if(this.getParent()!=null){
			return this.getParent().getParameter(name, defaultValue);
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IParameterize#getParameterNames()
	 */
	@Override
	public String[] getParameterNames() {
		return this.params.keySet().toArray(new String[this.params.size()]);
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IParameterize#hasParameter(java.lang.String)
	 */
	@Override
	public boolean hasParameter(String name) {
		boolean e = this.params.containsKey(name);
		if(!e){
			if(this.getParent()!=null){
				e = this.getParent().hasParameter(name);
			}
		}
		return e;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IParameterize#removeParameter(java.lang.String)
	 */
	@Override
	public void removeParameter(String name) {
		this.params.remove(name);
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IParameterize#setParameter(java.lang.String, java.lang.String)
	 */
	@Override
	public void setParameter(String name, String value) {
		this.params.put(name, new SimpleParameter(name,value));
	}
	
	@Override
	public void setParameter(IParameter parameter) {
		if(parameter!=null){
			this.params.put(parameter.getName(), parameter);
		}
	}

	@Override
	public IParameterize getParent() {
		return this.parent;
	}


	@Override
	public void setParent(IParameterize parent) {
		this.parent = parent;
	}
	
	

}
