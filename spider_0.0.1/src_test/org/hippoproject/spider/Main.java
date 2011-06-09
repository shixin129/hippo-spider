/**
 * 
 */
package org.hippoproject.spider;


/**
 * @author shixin
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		ISpider spider = DefaultSpiderManager.getInstance().getSpider("E:/workspace/ide_workspace/workspace_v3.4/spider_0.0.1/examples/config_example_01.xml");
		spider.run(SpiderContext.getDefault());
	}

}
