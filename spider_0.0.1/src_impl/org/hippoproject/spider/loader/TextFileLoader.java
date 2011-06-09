/**
 * 
 */
package org.hippoproject.spider.loader;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.hippoproject.spider.ISpider;
import org.hippoproject.spider.SpiderContext;
import org.hippoproject.spider.transformer.TransformField;

/**
 * @author shixin
 *
 */
public class TextFileLoader extends BaseLoader {
	
	public static final String BEHAVIOR = "text-file";
	public static final String FILE = "file";
	public static final String MODE = "mode";
	private PrintWriter writer;

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.loader.ILoader#after(org.hippoproject.spider.ISpider, org.hippoproject.spider.SpiderContext)
	 */
	@Override
	public void after(ISpider spider, SpiderContext context) throws Exception {
		if(this.getTemplatePostSection()!=null){
			this.writer.println(this.getTemplatePostSection());
		}
		this.writer.close();
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.loader.ILoader#before(org.hippoproject.spider.ISpider, org.hippoproject.spider.SpiderContext)
	 */
	@Override
	public void before(ISpider spider, SpiderContext context) throws Exception {
		String file = this.getParameter(FILE);
		String mode = this.getParameter(MODE,"renew");
		this.writer = new PrintWriter(new FileWriter(file,"append".equalsIgnoreCase(mode)));
		
		context.getLogger().info("================================================================");
		context.getLogger().info("  open output file (path="+file+")");
		context.getLogger().info("================================================================");
		
		if(this.getTemplatePreSection()!=null){
			this.writer.println(this.getTemplatePreSection());
		}
		
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.loader.ILoader#getBehaviorName()
	 */
	@Override
	public String getBehaviorName() {
		return BEHAVIOR;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.loader.ILoader#item(java.lang.Object, org.hippoproject.spider.transformer.TransformField[], java.lang.Object[], org.hippoproject.spider.ISpider, org.hippoproject.spider.SpiderContext)
	 */
	@Override
	public void item(Object item, TransformField[] fields,	Object[] fieldValues, ISpider spider, SpiderContext context) throws Exception {
		if(this.getTemplateBodySection()!=null){
			String text = buildTextTemplate(this.getTemplateBodySection().trim(), fields, fieldValues);
			this.writer.println(text);
		}
	}

}
