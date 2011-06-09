/**
 * 
 */
package org.hippoproject.spider;

import java.net.URL;

/**
 * @author shixin
 *
 */
public class SpiderHelper {

	public static String resolveUrl(String contextUrl, String destUrl){
    	String dUrl = destUrl;
    	try{
	    	if(!dUrl.toLowerCase().startsWith("http://") && !dUrl.toLowerCase().startsWith("https://")){
				String upath = contextUrl;
				int qIdx = upath.indexOf("?");
				if(qIdx!=-1){
					upath = upath.substring(0, qIdx);
				}
				URL urlPath = new URL(upath);
				
				if(!dUrl.startsWith("?")){
					dUrl = new URL(urlPath, dUrl).toString();
				}else{
					dUrl = urlPath.toString() + dUrl;
				}
			}
    	}catch(Exception ex){
    		
    	}
		return dUrl;
    }
	
	public static String replaceStr(String orgstr, String findstr, String rplstr)
    {
        if(orgstr != null && orgstr.length() > 0 && findstr != null && findstr.length() > 0 && findstr.length() <= orgstr.length() && rplstr != null)
        {
            String tmp = findstr.replaceAll("\\[", "\\\\[");
            tmp = tmp.replaceAll("\\]", "\\\\]");
            tmp = tmp.replaceAll("\\(", "\\\\(");
            tmp = tmp.replaceAll("\\)", "\\\\)");
            tmp = tmp.replaceAll("\\.", "\\\\.");
            tmp = tmp.replaceAll("\\^", "\\\\^");
            tmp = tmp.replaceAll("\\?", "\\\\?");
            tmp = tmp.replaceAll("\\*", "\\\\*");
            tmp = orgstr.replaceAll(tmp, rplstr);
            return tmp;
        } else
        {
            return orgstr;
        }
    }
	
	
	
}
