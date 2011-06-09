/**
 * 
 */
package org.hippoproject.spider.extracter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.htmlcleaner.HtmlCleaner;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.sun.org.apache.bcel.internal.generic.DCONST;

/**
 * @author shixin
 *
 */
public class ContentCaptureResponse implements IContentCaptureResponse {

	private int code;
	private String charset;
	private InputStream inputStream;
	private String contentText;
	private boolean contentTextInitl;
	private Document contentDocument;
	private boolean contentDocumentInitl;
	private HttpMethodBase method;
	private int docType;
	
	public ContentCaptureResponse(int code, String charset, InputStream inputStream, HttpMethodBase method ) {
		super();
		this.code = code;
		this.charset = charset;
		this.inputStream = inputStream;
		this.method = method;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IContentCaptureResponse#getCharset()
	 */
	public String getCharset() {
		return this.charset;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IContentCaptureResponse#getCode()
	 */
	public int getCode() {
		return this.code;
	}
	
	

	public HttpMethodBase getMethod() {
		return method;
	}

	@Override
	public boolean isOk() {
		return this.code == HttpStatus.SC_OK;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IContentCaptureResponse#getContentText()
	 */
	public String getContentText() {
		if(this.contentTextInitl){
			return this.contentText;
		}
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(this.inputStream,this.charset));
			StringBuilder sbf = new StringBuilder();
			String line = null;
			while((line=br.readLine())!=null){
				sbf.append(line);
			}
			this.contentText = sbf.toString();
			this.contentTextInitl = true;
			return this.contentText;
		}catch(Exception ex){
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IContentCaptureResponse#getInputStream()
	 */
	public InputStream getInputStream() {
		return this.inputStream;
	}
	
	

	public Document getDocument() {
		if(this.contentDocumentInitl){
			return this.contentDocument;
		}
		
		if(this.docType == DOC_TYPE_HTML){
			HtmlCleaner cleaner = new HtmlCleaner(this.getContentText());
			try {
				this.contentDocumentInitl = true;
				cleaner.clean();
				this.contentDocument = cleaner.createDOM();
				return this.contentDocument;
			} catch (Exception e) {
				return null;
			}
		}else if(this.docType == DOC_TYPE_XML){
			try {
				this.contentDocumentInitl = true;
				StringReader reader = new StringReader(getContentText());
				DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				this.contentDocument = builder.parse(new InputSource(reader));
				reader.close();
				return this.contentDocument;
			} catch (Exception e) {
				return null;
			} 
		}
		return null;
		
	}
	
	@Override
	public int getDocType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDocType(int type) {
		this.docType = type;
	}

	@Override
	public String toString() {
		StringBuilder sbf = new StringBuilder("ContentCaptureResponse{Code=");
		sbf.append(this.code).append("}");
		return sbf.toString();
	}

	
	
}
