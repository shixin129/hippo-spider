/**
 * 
 */
package org.hippoproject.spider.extracter;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpMethodBase;
import org.w3c.dom.Document;

/**
 * @author shixin
 *
 */
public interface IContentCaptureResponse {
	
	public static final int DOC_TYPE_HTML = 0;
	public static final int DOC_TYPE_XML = 1;

	public boolean isOk();
	
	public int getCode();
	
	public String getCharset();
	
	public InputStream getInputStream();
	
	public String getContentText();
	
	public Document getDocument();
	
	public HttpMethodBase getMethod();
	
	public void setDocType(int type);
	
	public int getDocType();
	
}
