/**
 * 
 */
package org.hippoproject.spider.extracter;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.hippoproject.spider.extracter.IContentCaptureRequest;

/**
 * @author shixin
 *
 */
public class ContentCaptureRequest implements IContentCaptureRequest {
	
	private String url;
	private String method;
	private String charset;
	private Map<String, String> datas;
	private Map<String, String> headers;
	
	public ContentCaptureRequest(String url) {
		super();
		this.url = url;
		this.method = "get";
		this.datas = new HashMap<String, String>();
		this.headers = new HashMap<String, String>();
	}
	
	

	@Override
	public String getCharset() {
		return this.charset;
	}



	@Override
	public void setCharset(String charset) {
		this.charset = charset;
	}



	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IContentCaptureRequest#getDatas()
	 */
	public Map<String, String> getDatas() {
		return this.datas;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IContentCaptureRequest#getHeaders()
	 */
	public Map<String, String> getHeaders() {
		return this.headers;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IContentCaptureRequest#getUrl()
	 */
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	

}
