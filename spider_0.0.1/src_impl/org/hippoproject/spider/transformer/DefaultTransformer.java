/**
 * 
 */
package org.hippoproject.spider.transformer;

import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.hippoproject.spider.BaseParameterize;
import org.hippoproject.spider.DefaultSpiderManager;
import org.hippoproject.spider.ISpider;
import org.hippoproject.spider.SpiderHelper;
import org.hippoproject.spider.extracter.IContentCaptureRequest;
import org.hippoproject.spider.extracter.IContentCaptureResponse;
import org.hippoproject.spider.extracter.IExtracter;
import org.w3c.dom.Node;

/**
 * @author shixin
 *
 */
public class DefaultTransformer extends BaseParameterize implements	ITransformer {
	
	public static final String BEHAVIOR = "default";

	private Map<String, TransformField> fields = new HashMap<String, TransformField>();
	private ScriptEngine scriptEngine;
	
	public DefaultTransformer() {
		ScriptEngineManager factory = new ScriptEngineManager();
		this.scriptEngine = factory.getEngineByName("JavaScript");
		//this.scriptEngine.put("x", new TransformerToolbox());
	}
	
	@Override
	public String getBehaviorName() {
		return BEHAVIOR;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.transformer.ITransformer#addField(org.hippoproject.spider.transformer.TransformField)
	 */
	@Override
	public void addField(TransformField field) {
		if(field!=null){
			this.fields.put(field.getName(), field);
		}
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.transformer.ITransformer#findField(java.lang.String)
	 */
	@Override
	public TransformField findField(String fieldName) {
		return this.fields.get(fieldName);
	}


	/* (non-Javadoc)
	 * @see org.hippoproject.spider.transformer.ITransformer#getFields()
	 */
	@Override
	public TransformField[] getFields() {
		return this.fields.values().toArray(new TransformField[this.fields.size()]);
	}
	
	/* (non-Javadoc)
	 * @see org.hippoproject.spider.transformer.ITransformer#getFieldValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object getFieldValue(Object item, String fieldName, ISpider spider) throws Exception {
		TransformField field = this.findField(fieldName);
		if("text".equalsIgnoreCase(field.getType())){
			if(item instanceof IContentCaptureResponse){
				IContentCaptureResponse response = (IContentCaptureResponse) item;
				return response.getContentText();
			}
			return "";
		}else if("document".equalsIgnoreCase(field.getType())){
			if(item instanceof IContentCaptureResponse){
				IContentCaptureResponse response = (IContentCaptureResponse) item;
				return response.getDocument();
			}
			return null;
		}else if("node".equalsIgnoreCase(field.getType())){
			if(item instanceof Node){
				return item;
			}
			return null;
		}else if("script".equalsIgnoreCase(field.getType())){
			Bindings bds = this.scriptEngine.createBindings();
			bds.put("x", new TransformerToolbox(item, spider));
			bds.put("item", item);
			return this.scriptEngine.eval(DefaultSpiderManager.getInstance().resolveVariableValue(field.getExpression().trim(),this), bds);
		}else if("xpath".equalsIgnoreCase(field.getType())){//xpath
			return XPathFactory.newInstance().newXPath().evaluate(DefaultSpiderManager.getInstance().resolveVariableValue(field.getExpression().trim(),this), item, XPathConstants.STRING);
		}else if("xpath-nodes".equalsIgnoreCase(field.getType())){//xpath
			return XPathFactory.newInstance().newXPath().evaluate(DefaultSpiderManager.getInstance().resolveVariableValue(field.getExpression().trim(),this), item, XPathConstants.NODESET);
		}else if("xpath-node".equalsIgnoreCase(field.getType())){//xpath
			return XPathFactory.newInstance().newXPath().evaluate(DefaultSpiderManager.getInstance().resolveVariableValue(field.getExpression().trim(),this), item, XPathConstants.NODE);
		}else if("xpath-url".equalsIgnoreCase(field.getType())){
			String url = (String) XPathFactory.newInstance().newXPath().evaluate(DefaultSpiderManager.getInstance().resolveVariableValue(field.getExpression().trim(),this), item, XPathConstants.STRING);
			IContentCaptureRequest request = spider.getExtracter().getCurrentRequest();
			if(request!=null){
				return SpiderHelper.resolveUrl(request.getUrl(), url);
			}
			return url;
		}
		return null;
	}


}
