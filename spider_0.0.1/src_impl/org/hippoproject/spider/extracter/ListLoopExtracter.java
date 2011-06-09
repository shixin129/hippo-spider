/**
 * 
 */
package org.hippoproject.spider.extracter;

import java.net.URL;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.hippoproject.spider.BaseParameterize;
import org.hippoproject.spider.ISpider;
import org.hippoproject.spider.SpiderContext;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * @author shixin
 *
 */
public class ListLoopExtracter extends BaseParameterize implements IExtracter {
	
	public static final String BEHAVIOR = "list-loop";
	public static final String P_URL = "pageUrl";
	public static final String P_ITEMS_XPATH = "itemsXPath";
	public static final String P_NEXT_XPATH = "nextXPath";
	public static final String P_MAX_LOOP = "maxLoops";
	
	
	private IContentCapturer capturer;
	private IContentCaptureRequest currentRequest;
	private ICaptureHandler captureHandler;


	/* (non-Javadoc)
	 * @see org.hippoproject.spider.extracter.IExtracter#getBehaviorName()
	 */
	@Override
	public String getBehaviorName() {
		return BEHAVIOR;
	}

	@Override
	public IContentCapturer getCapturer() {
		return capturer;
	}



	/* (non-Javadoc)
	 * @see org.hippoproject.spider.extracter.IExtracter#extract(org.hippoproject.spider.extracter.IExtracterHandler, org.hippoproject.spider.ILogger)
	 */
	@Override
	public void extract(IExtracterHandler handler, ISpider spider, SpiderContext context) {
		int loopIndex = 0;
		int maxLoop = 0;
		String maxLoopStr = this.getParameter(P_MAX_LOOP);
		if(maxLoopStr!=null && maxLoopStr.length()>0){
			try{
				maxLoop = Integer.parseInt(maxLoopStr);
			}catch(Exception ex){};
		}
		
		String[] urlList = new String[]{};
		String urls = this.getParameter(P_URL);
		if(urls!=null && urls.length()>0){
			urlList = urls.split("\\|\\|");
		}
		
		for(int j=0,m=urlList.length;j<m;j++){
			
			URL lastUrl = null;
			String url = urlList[j].trim();
			
			while(url!=null){
				
				if("true".equalsIgnoreCase(this.getParameter("useFilter","")) && lastUrl==null){
					capturer = new FilterContentCapturer();
				}else{
					capturer = new DefaultContentCapturer();
				}
				
				try {
					if(!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://")){
						String upath = lastUrl.toString();
						int qIdx = upath.indexOf("?");
						if(qIdx!=-1){
							upath = upath.substring(0, qIdx);
						}
						URL urlPath = new URL(upath);
						
						if(!url.startsWith("?")){
							url = new URL(urlPath, url).toString();
						}else{
							url = urlPath.toString() + url;
						}
					}
					lastUrl = new URL(url);
				} catch (Exception e) {
					handler.handleException(e);
					return;
				}
				
				
				context.getLogger().info("================================================================");
				context.getLogger().info("  extract page (url="+url+")");
				context.getLogger().info("================================================================");
				
				
				this.currentRequest = new ContentCaptureRequest(url);
			
				if(!ISpider.CHARSET_AUTO.equalsIgnoreCase(spider.getCharset())){
					currentRequest.setCharset(spider.getCharset());
				}
				if(this.captureHandler!=null){
					this.captureHandler.handleRequest(currentRequest);
				}
				IContentCaptureResponse response = null;
				try {
					response = capturer.capture(currentRequest);
				} catch (Exception e) {
					handler.handleException(e);
					return;
				}
				
				if(this.captureHandler!=null){
					this.captureHandler.handleResponse(response);
				}
				
				if(!response.isOk()){
					handler.handleException(new Exception("Http response error (Code="+response.getCode()+",Url="+url+")"));
				}
				
				context.getLogger().info("################################################################");
				//context.getLogger().info(response.getContentText());
				context.getLogger().info("################################################################");
				
				Document document = response.getDocument();
				NodeList items = null;
				try {
					items = (NodeList) XPathFactory.newInstance().newXPath().evaluate(this.getParameter(P_ITEMS_XPATH), document, XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					handler.handleException(e);
					return;
				}
				
				
				for(int i=0,n=items.getLength();i<n;i++){
					handler.handleItem(items.item(i));
					loopIndex++;
					if(maxLoop>0 && maxLoop<=loopIndex){
						return;
					}
				}
				
				url = null;
			
				//到下一页
				String nextPath = this.getParameter(P_NEXT_XPATH);
				if(nextPath!=null && nextPath.trim().length()>0){
					try {
						String nextUrl = (String) XPathFactory.newInstance().newXPath().evaluate(nextPath,document,XPathConstants.STRING);
						if(nextUrl!=null && nextUrl.trim().length()>0){
							url = nextUrl;
						}
					} catch (XPathExpressionException e) {
						handler.handleException(e);
						
					}
				}
			}
			
		}
		
		
		
	}

	@Override
	public void setCaptureHandler(ICaptureHandler handler) {
		this.captureHandler = handler;
	}

	@Override
	public IContentCaptureRequest getCurrentRequest() {
		return this.currentRequest;
	}
}
