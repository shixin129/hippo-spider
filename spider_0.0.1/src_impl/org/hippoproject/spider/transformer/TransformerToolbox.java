/**
 * 
 */
package org.hippoproject.spider.transformer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.hippoproject.spider.ISpider;
import org.hippoproject.spider.SpiderHelper;
import org.hippoproject.spider.extracter.ContentCaptureRequest;
import org.hippoproject.spider.extracter.DefaultContentCapturer;
import org.hippoproject.spider.extracter.IContentCaptureRequest;
import org.hippoproject.spider.extracter.IContentCaptureResponse;
import org.hippoproject.spider.extracter.IContentCapturer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author shixin
 *
 */
public class TransformerToolbox {
	
	private Object item;
	private ISpider spider;
	private String charset;
	
	public TransformerToolbox(Object item, ISpider spider) {
		super();
		this.item = item;
		this.spider = spider;
	}
	
	public TransformerToolbox(Object item) {
		super();
		this.item = item;
		this.spider = null;
	}
	
	public TransformerToolbox() {
	}
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String data(String path) throws Exception{
		return (String) XPathFactory.newInstance().newXPath().evaluate(path, this.item, XPathConstants.STRING);
	}
	
	public Node nodeData(String path) throws Exception{
		return (Node) XPathFactory.newInstance().newXPath().evaluate(path, this.item, XPathConstants.NODE);
	}
	
	public NodeList nodesData(String path) throws Exception{
		return (NodeList) XPathFactory.newInstance().newXPath().evaluate(path, this.item, XPathConstants.NODESET);
	}
	
	public String html(String xpath){
		return this.fetchHtmlContentByXPath(this.item, xpath, null, true);
	}
	
	public String html(String xpath, String excludeTagNames){
		return this.fetchHtmlContentByXPath(this.item, xpath, excludeTagNames, true);
	}
	
	public String html(String xpath, String excludeTagNames, boolean autoExcludeRootNode){
		return this.fetchHtmlContentByXPath(this.item, xpath, excludeTagNames, autoExcludeRootNode);
	}
	
	public boolean loadUrl(String url){
		IContentCapturer capturer = null;
		if(this.spider!=null){
			capturer = this.spider.getExtracter().getCapturer();
		}
		if(capturer == null){
			capturer = new DefaultContentCapturer();
		}
		
		
		IContentCaptureRequest creq = new ContentCaptureRequest(url);
		if(this.spider!=null){
			if(!ISpider.CHARSET_AUTO.equalsIgnoreCase(spider.getCharset())){
				creq.setCharset(spider.getCharset());
			}
		}else{
			if(this.charset!=null){
				creq.setCharset(charset);
			}
		}
		IContentCaptureResponse response = null;
		try {
			response = capturer.capture(creq);
		} catch (Exception e) {
			return false;
		}
		
		if(!response.isOk()){
			return false;
		}
		
		Document document = response.getDocument();
		if(document==null) return false;
		
		this.item = document;
		
		return true;
	}
	
	
	public String fetchString(String url, String xpath){
		IContentCapturer capturer = null;
		if(this.spider!=null){
			capturer = this.spider.getExtracter().getCapturer();
		}
		if(capturer == null){
			capturer = new DefaultContentCapturer();
		}
		
		
		IContentCaptureRequest creq = new ContentCaptureRequest(url);
		if(this.spider!=null){
			if(!ISpider.CHARSET_AUTO.equalsIgnoreCase(spider.getCharset())){
				creq.setCharset(spider.getCharset());
			}
		}else{
			if(this.charset!=null){
				creq.setCharset(charset);
			}
		}
		IContentCaptureResponse response = null;
		try {
			response = capturer.capture(creq);
		} catch (Exception e) {
			return "";
		}
		
		if(!response.isOk()){
			return "";
		}
		
		Document document = response.getDocument();
		if(document==null) return "";
		
		String ret = "";
		
		try {
			ret = (String) XPathFactory.newInstance().newXPath().evaluate(xpath, document, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			return "";
		}
		
		return ret;
	}
	
	
	private String fetchHtmlContentByXPath(Object obj, String xpath, String excludeTagNames, boolean autoExcludeRootNode){
		NodeList nList = null;
		
		try {
			nList = (NodeList) XPathFactory.newInstance().newXPath().evaluate(xpath, obj, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			return "";
		}
		
		if(nList == null || nList.getLength()==0) return "";
		
		if(nList.getLength()==1 && autoExcludeRootNode){
			nList = nList.item(0).getChildNodes();
			if(nList == null || nList.getLength()==0) return "";
		}
		
		StringWriter stringWriter = new StringWriter();
		TransformerFactory tfactory = TransformerFactory.newInstance();
		
		
		StreamResult result = new StreamResult(stringWriter);

		Transformer transformer = null;
		try {
			transformer = tfactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "html");
			transformer.setOutputProperty(OutputKeys.VERSION, "4.0");
		} catch (TransformerConfigurationException e) {
			return "";
		}
		
		
		for(int i=0;i<nList.getLength();i++){
			Node node = nList.item(i);
			if(filterTags(node.getNodeName(), excludeTagNames)){
				DOMSource source = new DOMSource(nList.item(i));
				try {
					transformer.transform(source, result);
				} catch (TransformerException e) {
				}
			}
		}
		
		
		return replaceHtmlStr(stringWriter.toString());
	}
	
	public String fetchHtmlContent(String url, String xpath){
		return fetchHtmlContent(url, xpath, null);
	}
	
	public String fetchHtmlContent(String url, String xpath, String excludeTagNames){
		
		IContentCapturer capturer = null;
		if(this.spider!=null){
			capturer = this.spider.getExtracter().getCapturer();
		}
		if(capturer == null){
			capturer = new DefaultContentCapturer();
		}
		
		
		IContentCaptureRequest creq = new ContentCaptureRequest(url);
		if(this.spider!=null){
			if(!ISpider.CHARSET_AUTO.equalsIgnoreCase(spider.getCharset())){
				creq.setCharset(spider.getCharset());
			}
		}else{
			if(this.charset!=null){
				creq.setCharset(charset);
			}
		}
		IContentCaptureResponse response = null;
		try {
			response = capturer.capture(creq);
		} catch (Exception e) {
			return "";
		}
		
		if(!response.isOk()){
			return "";
		}
		
		Document document = response.getDocument();
		if(document==null) return "";
		
		return this.fetchHtmlContentByXPath(document, xpath, excludeTagNames, true);
	}
	
	private boolean filterTags(String tagName, String tagNames){
		if(tagNames==null) return true;
		if(tagName==null) return true;
		return tagNames.indexOf(tagName)==-1;
	}
	
	private String replaceHtmlStr(String xml){
		if(xml == null || xml.length()==0) return xml;
		String tmp = SpiderHelper.replaceStr(xml, "&amp;", "&");
		return tmp;
	}
	
}
