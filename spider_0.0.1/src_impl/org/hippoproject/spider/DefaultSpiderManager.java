/**
 * 
 */
package org.hippoproject.spider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.hippoproject.spider.extracter.IExtracter;
import org.hippoproject.spider.extracter.ListLoopExtracter;
import org.hippoproject.spider.loader.ILoader;
import org.hippoproject.spider.loader.TextFileLoader;
import org.hippoproject.spider.transformer.DefaultTransformer;
import org.hippoproject.spider.transformer.ITransformer;
import org.hippoproject.spider.transformer.TransformField;
import org.hippoproject.spider.transformer.TransformerToolbox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author shixin
 *
 */
public class DefaultSpiderManager implements ISpiderManager {
	
	private Map<String, Class<IExtracter>> extracters;
	private Map<String, Class<ITransformer>> transformers;
	private Map<String, Class<ILoader>> loaders;
	
	private ScriptEngine scriptEngine;
	
	private static ISpiderManager instance = null;
	
	private DefaultSpiderManager() {
		
		this.extracters = new HashMap<String, Class<IExtracter>>();
		this.transformers = new HashMap<String, Class<ITransformer>>();
		this.loaders = new HashMap<String, Class<ILoader>>();
		
		
		ScriptEngineManager factory = new ScriptEngineManager();
		this.scriptEngine = factory.getEngineByName("JavaScript");
		
		this.registerExtracter(ListLoopExtracter.BEHAVIOR, ListLoopExtracter.class);
		this.registerTransformer(DefaultTransformer.BEHAVIOR, DefaultTransformer.class);
		this.registerLoader(TextFileLoader.BEHAVIOR, TextFileLoader.class);
		
		
		
	}
	
	public static ISpiderManager getInstance(){
		if(instance==null){
			instance = new DefaultSpiderManager();
		}
		return instance;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#findExtracter(java.lang.String)
	 */
	@Override
	public IExtracter findExtracter(String behaviorName) {
		if(this.extracters.containsKey(behaviorName)){
			return (IExtracter) this.getInstance(this.extracters.get(behaviorName));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#findLoader(java.lang.String)
	 */
	@Override
	public ILoader findLoader(String behaviorName) {
		if(this.loaders.containsKey(behaviorName)){
			return (ILoader) this.getInstance(this.loaders.get(behaviorName));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#findTransformer(java.lang.String)
	 */
	@Override
	public ITransformer findTransformer(String behaviorName) {
		if(this.transformers.containsKey(behaviorName)){
			return (ITransformer) this.getInstance(this.transformers.get(behaviorName));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#getSpider(java.io.InputStream)
	 */
	@Override
	public ISpider getSpider(InputStream stream) throws Exception {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = fac.newDocumentBuilder();
		Document doc = builder.parse(stream);
		return this.getSpider(doc);
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#getSpider(java.lang.String)
	 */
	@Override
	public ISpider getSpider(String filePath) throws Exception {
		return this.getSpider(new FileInputStream(filePath));
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#getSpider(java.lang.String, org.hippoproject.spider.extracter.IExtracter, org.hippoproject.spider.transformer.ITransformer, org.hippoproject.spider.loader.ILoader)
	 */
	@Override
	public ISpider getSpider(String charset, IExtracter extracter, ITransformer transformer, ILoader loader) {
		return new DefaultSpider(charset,extracter,transformer, loader);
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#registerExtracter(java.lang.String, java.lang.Class)
	 */
	@Override
	public void registerExtracter(String behaviorName, Class clz) {
		this.extracters.put(behaviorName, clz);
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#registerLoader(java.lang.String, java.lang.Class)
	 */
	@Override
	public void registerLoader(String behaviorName, Class clz) {
		this.loaders.put(behaviorName, clz);
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpiderManager#registerTransformer(java.lang.String, java.lang.Class)
	 */
	@Override
	public void registerTransformer(String behaviorName, Class clz) {
		this.transformers.put(behaviorName, clz);
	}
	
	private ISpider getSpider(Document document) throws Exception{
		
		
		//spider local
		Element rootElement = document.getDocumentElement();
		String charset = rootElement.getAttribute("charset");
		if(charset==null || charset.trim().length()==0){
			charset = ISpider.CHARSET_AUTO;
		}
		

		
		//extracter
		IExtracter extracter = null;
		Element elExtracter = this.getChildElementByTagName(rootElement, "extracter");
		if(elExtracter!=null){
			String extBehName = elExtracter.getAttribute("behavior");
			extracter = this.findExtracter(extBehName);
			if(extracter!=null){
				Element elParams = this.getChildElementByTagName(elExtracter, "params");
				if(elParams!=null){
					this.setupParameters(extracter, elParams);
				}
			}
		}
		if(extracter==null){
			throw new Exception("can not find extracter.");
		}
		
		//transformer
		ITransformer transformer = null;
		Element elTransformer = this.getChildElementByTagName(rootElement, "transformer");
		if(elTransformer!=null){
			String trmBehName = elTransformer.getAttribute("behavior");
			transformer = this.findTransformer(trmBehName);
			if(transformer!=null){
				Element elParams = this.getChildElementByTagName(elTransformer, "params");
				if(elParams!=null){
					this.setupParameters(transformer, elParams);
				}
			}
			if(transformer!=null){
				Element elFields = this.getChildElementByTagName(elTransformer, "fields");
				if(elFields!=null){
					NodeList nlField = elFields.getElementsByTagName("field");
					if(nlField!=null)for(int i=0,n=nlField.getLength();i<n;i++){
						Element elField = (Element) nlField.item(i);
						String fName = elField.getAttribute("name");
						String fType = elField.getAttribute("type");
						String fExp = elField.getTextContent();
						transformer.addField(new TransformField(fName, fType, fExp));
					}
				}
			}
		}
		if(transformer==null){
			throw new Exception("can not find transformer.");
		}
		
		//loader
		ILoader loader = null;
		Element elLoader = this.getChildElementByTagName(rootElement, "loader");
		if(elLoader!=null){
			String ldBehName = elLoader.getAttribute("behavior");
			loader = this.findLoader(ldBehName);
			if(loader!=null){
				Element elParams = this.getChildElementByTagName(elLoader, "params");
				if(elParams!=null){
					this.setupParameters(loader, elParams);
				}
			}
			
			if(loader!=null){
				Element elTemplate = this.getChildElementByTagName(elLoader, "template");
				if(elTemplate!=null){
					Element elPre = this.getChildElementByTagName(elTemplate, "pre");
					Element elPost = this.getChildElementByTagName(elTemplate, "post");
					Element elBody = this.getChildElementByTagName(elTemplate, "body");
					if(elPre!=null) loader.setTemplatePreSection(elPre.getTextContent());
					if(elPost!=null) loader.setTemplatePostSection(elPost.getTextContent());
					if(elBody!=null) loader.setTemplateBodySection(elBody.getTextContent());
				}
			}
		}
		if(loader==null){
			throw new Exception("can not find loader.");
		}
		
		DefaultSpider spider = new DefaultSpider(charset,extracter,transformer, loader);
		
		//load spider params
		Element spParams = this.findParamsElement(rootElement);
		if(spParams!=null){
			this.setupParameters(spider, spParams);
		}
		
		return spider;
	}
	
	
	@Override
	public IParameter createParameter(String name, String type, String value) {
		IParameter parameter = null;
		if("script".equalsIgnoreCase(type)){
			parameter = new ScriptParameter(name, value);
		}else{
			parameter = new SimpleParameter(name, value);
		}
		return parameter;
	}
	
	@Override
	public String resolveVariableValue(String value, final IParameterize parameterize) {
		return this.resolveVariableValue(value, "\\$", new IVariableResolver(){
			@Override
			public String resolve(String name) {
				return parameterize.getParameter(name,"");
			}});
	}

	@Override
	public String resolveVariableValue(String value, String varFlag, IVariableResolver resolver) {
		if(value==null || value.length()==0){
			return "";
		}
		String REG_EXP = varFlag + "\\{([^\\}]+)\\}";
		Pattern REG_PATTERN = Pattern.compile(REG_EXP);
		
		StringBuffer sbf = new StringBuffer();
		Matcher templateMatcher = REG_PATTERN.matcher(value);
		while(templateMatcher.find()){
			String name = templateMatcher.group(1);
			Object val = resolver.resolve(name);
			templateMatcher.appendReplacement(sbf, val!=null?val.toString():"");
		}
		templateMatcher.appendTail(sbf);
		return sbf.toString();
	}
	
	@Override
	public Object evalVariableValue(String value, Map<String, Object> contextBeans) {
		Bindings bds = this.scriptEngine.createBindings();
		bds.putAll(contextBeans);
		try {
			return this.scriptEngine.eval(value, bds);
		} catch (ScriptException e) {
			return null;
		}
	}

	private Element findParamsElement(Element element){
		try {
			return (Element) XPathFactory.newInstance().newXPath().evaluate("./params", element, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			return null;
		}
	}
	
	private NodeList findParamElements(Element element){
		try {
			return (NodeList) XPathFactory.newInstance().newXPath().evaluate("./param", element, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			return null;
		}
	}

	private void setupParameters(IParameterize parameterize, Element paramsElement){
		NodeList nl = this.findParamElements(paramsElement);// paramsElement.getElementsByTagName("param");
		if(nl!=null)for(int i=0,n=nl.getLength();i<n;i++){
			Element elp = (Element) nl.item(i);
			String name = elp.getAttribute("name");
			String type = elp.getAttribute("type");
			String value = elp.getTextContent();
			parameterize.setParameter(this.createParameter(name, type, value));
		}
	}
	
	private Element getChildElementByTagName(Element element, String tagName){
		NodeList nl = element.getElementsByTagName(tagName);
		if(nl!=null && nl.getLength()==1){
			return (Element) nl.item(0);
		}
		return null;
	}
	
	
	private Object getInstance(Class clz){
		try {
			return clz.newInstance();
		} catch (Exception e) {
			return null;
		}
	}
	
	
	

}
