/**
 * 
 */
package org.hippoproject.spider;

import java.io.File;

/**
 * @author shixin
 *
 */
public class MainRunner {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		if(args.length!=1){
			System.out.println("Usage : java -jar spider.jar <configuration file path>");
			System.exit(0);
		}
		
		File config = new File(args[0]);
		if(!config.exists()){
			System.out.println("configuration file <"+args[0]+"> does not exist.");
			System.exit(0);
		}
		
		ISpider spider = DefaultSpiderManager.getInstance().getSpider(args[0]);
		spider.run(SpiderContext.getDefault());
		
	}

}
