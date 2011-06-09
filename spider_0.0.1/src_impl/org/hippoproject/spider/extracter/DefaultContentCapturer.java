/**
 * 
 */
package org.hippoproject.spider.extracter;

import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author shixin
 *
 */
public class DefaultContentCapturer implements IContentCapturer {

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.IContentCapturer#capture(org.hippoproject.spider.IContentCaptureRequest)
	 */
	@SuppressWarnings("unchecked")
	public IContentCaptureResponse capture(IContentCaptureRequest request)	throws Exception {

		
		HttpClient httpClient = new HttpClient();
		
		HttpMethodBase method = null;
		
		if("get".equalsIgnoreCase(request.getMethod())){
			GetMethod gm = new GetMethod(request.getUrl());
			if(request.getDatas().size()>0){
				Object[] ents = request.getDatas().entrySet().toArray();
				NameValuePair[] vps = new NameValuePair[ents.length];
				for(int i=0;i<ents.length;i++){
					Entry<String, String> ent = (Entry<String, String>) ents[i];
					vps[i] = new NameValuePair(ent.getKey(),ent.getValue());
				}
				gm.setQueryString(vps);
			}
			
			method = gm;
		}else{
			PostMethod pm = new PostMethod(request.getUrl());
			if(request.getDatas().size()>0){
				Object[] ents = request.getDatas().entrySet().toArray();
				NameValuePair[] vps = new NameValuePair[ents.length];
				for(int i=0;i<ents.length;i++){
					Entry<String, String> ent = (Entry<String, String>) ents[i];
					vps[i] = new NameValuePair(ent.getKey(),ent.getValue());
				}
				pm.setRequestBody(vps);
			}
			method = pm;
		}
		
		if(request.getHeaders().size()>0){
			Object[] ents = request.getHeaders().entrySet().toArray();
			for(int i=0;i<ents.length;i++){
				Entry<String, String> ent = (Entry<String, String>) ents[i];
				method.setRequestHeader(ent.getKey(), ent.getValue());
			}
		}
		method.setRequestHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; GoogleT5; CIBA; .NET CLR 2.0.50727)");
		//method.setFollowRedirects(true);
		
		int statusCode = httpClient.executeMethod(method);
		
		IContentCaptureResponse ccr = null;
		
		if(statusCode==HttpStatus.SC_OK){
			ccr = new ContentCaptureResponse(statusCode, request.getCharset()==null?method.getResponseCharSet():request.getCharset(),method.getResponseBodyAsStream(), method);
		}else if(statusCode==HttpStatus.SC_MOVED_TEMPORARILY){
			Header[] headers = method.getResponseHeaders();
			Header location = method.getResponseHeader("Location");
			if(location!=null){
				String locationUrl = location.getValue();
				if(locationUrl!=null){
					IContentCaptureRequest creq = new ContentCaptureRequest(locationUrl);
					creq.setMethod("get");
					creq.setCharset(request.getCharset());
					
					Header cookies = method.getResponseHeader("Set-Cookie");
					if(cookies!=null){
						creq.getHeaders().put("Cookie", cookies.getValue());
					}
					
					ccr = this.capture(creq);
				}
			}
		}else{
			ccr = new ContentCaptureResponse(statusCode, null, null, null);
		}
		
		return ccr;
	}


	
	

}
