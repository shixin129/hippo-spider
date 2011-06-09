/**
 * 
 */
package org.hippoproject.spider.transformer;

import org.hippoproject.spider.ISpider;
import org.hippoproject.spider.extracter.ContentCaptureRequest;
import org.hippoproject.spider.extracter.DefaultContentCapturer;
import org.hippoproject.spider.extracter.IContentCaptureRequest;
import org.hippoproject.spider.extracter.IContentCaptureResponse;
import org.hippoproject.spider.extracter.IContentCapturer;

/**
 * @author shixin
 *
 */
public class TextTransformerToolbox {

	
	private IContentCaptureResponse response;
	private ISpider spider;
	private String charset;
	private boolean captureOk;
	
	
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public ISpider getSpider() {
		return spider;
	}

	public void setSpider(ISpider spider) {
		this.spider = spider;
	}
	
	

	public boolean isCaptureOk() {
		return captureOk;
	}


	public boolean loadUrl(String url){
		captureOk = false;
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
		
		this.response = response;
		
		captureOk = true;
		return true;
	}
	
	
	public String fetchString(String beginString, String endString, boolean igCase){
		
		if(!this.captureOk) return null;
		
		String str = this.response.getContentText();
		if(str==null) return null;
		
		int sIdx = -1;
		int eIdx = -1;
		String lstr = str;
		if(igCase){
			lstr = str.toLowerCase();
		}else{
			lstr = str;
		}
		
		if(beginString==null){
			sIdx = 0;
		}else{
			sIdx = lstr.indexOf(beginString.toLowerCase());
			if(sIdx!=-1){
				sIdx += beginString.length();
			}
		}
		if(endString==null){
			eIdx = lstr.length();
		}else{
			eIdx = lstr.indexOf(endString.toLowerCase(), sIdx);
		}
		
		if(sIdx!=-1 && eIdx!=-1 && eIdx>sIdx){
			return str.substring(sIdx, eIdx);
		}
		
		return null;
	}

	
}
