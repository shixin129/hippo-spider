/**
 * 
 */
package org.hippoproject.spider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author shixin
 *
 */
public class SpiderToolbox {

	private IParameterize parameterize;
	
	public SpiderToolbox(IParameterize parameterize) {
		super();
		this.parameterize = parameterize;
	}

	public String get(String name){
		return this.parameterize.getParameter(name);
	}
	
	public String datetime(String pt){
		return new SimpleDateFormat(pt).format(new Date());
	}
	
	public String uuid(){
		return UUID.randomUUID().toString();
	}
	
}
