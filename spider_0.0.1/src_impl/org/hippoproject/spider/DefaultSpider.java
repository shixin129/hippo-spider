/**
 * 
 */
package org.hippoproject.spider;

import java.util.Arrays;

import org.hippoproject.spider.extracter.IExtracter;
import org.hippoproject.spider.extracter.IExtracterHandler;
import org.hippoproject.spider.loader.ILoader;
import org.hippoproject.spider.transformer.ITransformer;
import org.hippoproject.spider.transformer.TransformField;

/**
 * @author shixin
 *
 */
public class DefaultSpider extends BaseParameterize implements ISpider {
	
	private String charset;
	private IExtracter extracter;
	private ITransformer transformer;
	private ILoader loader;
	
	public DefaultSpider(String charset, IExtracter extracter, ITransformer transformer, ILoader loader) {
		super();
		this.charset = charset;
		this.extracter = extracter;
		this.transformer = transformer;
		this.loader = loader;
		
		this.extracter.setParent(this);
		this.transformer.setParent(this);
		this.loader.setParent(this);
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpider#getCharset()
	 */
	@Override
	public String getCharset() {
		return this.charset;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpider#getExtracter()
	 */
	@Override
	public IExtracter getExtracter() {
		return this.extracter;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpider#getLoader()
	 */
	@Override
	public ILoader getLoader() {
		return this.loader;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpider#getTransformer()
	 */
	@Override
	public ITransformer getTransformer() {
		return this.transformer;
	}

	/* (non-Javadoc)
	 * @see org.hippoproject.spider.ISpider#run(org.hippoproject.spider.SpiderContext)
	 */
	@Override
	public void run(final SpiderContext context) throws Exception {
		
		final ILogger log = context.getLogger();
		
		log.info("now starting...");
		
		this.loader.before(this, context);
		
		this.getExtracter().extract(new IExtracterHandler(){

			@Override
			public void handleException(Throwable tx) {
				log.error("extract error.", tx);
			}

			@Override
			public void handleItem(Object item) {
				log.info("handle item => " + item);
				ITransformer t = DefaultSpider.this.getTransformer();
				ILoader l = DefaultSpider.this.getLoader();
				
				TransformField[] fields = t.getFields();
				Object[] fieldValues = new Object[fields.length];
				
				try{
					for(int i=0,n=fields.length;i<n;i++){
						fieldValues[i] = t.getFieldValue(item, fields[i].getName(), DefaultSpider.this);
					}
					
					log.info("transform item done. (" + Arrays.toString(fieldValues)+")");
					
					l.item(item, fields, fieldValues, DefaultSpider.this, context);
					log.info("load item done.");
				}catch(Exception ex){
					log.error("transform or load error.", ex);
				}
			}
			
		}, this, context);
		
		this.loader.after(this, context);
		
		log.info("now stopped.");
		
	}

}
