/**
 * 
 */
package org.hippoproject.spider.transformer;

/**
 * @author shixin
 *
 */
public class TransformField {

	private String name;
	private String type;
	private String expression;
	
	public TransformField() {
	}
	
	public TransformField(String name, String type, String expression) {
		super();
		this.name = name;
		this.type = type;
		this.expression = expression;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	
	
}
