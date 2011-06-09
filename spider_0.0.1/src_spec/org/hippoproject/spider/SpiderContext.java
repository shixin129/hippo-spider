/**
 * 
 */
package org.hippoproject.spider;

/**
 * @author shixin
 *
 */
public class SpiderContext {
	
	public static SpiderContext getDefault(){
		SpiderContext context = new SpiderContext(new ILogger(){
			@Override
			public void debug(String log) {
				this.log(TYPE_DEBUG, log);
			}
			@Override
			public void error(String log) {
				this.log(TYPE_ERROR, log);
			}
			@Override
			public void error(String log, Throwable tx) {
				this.log(TYPE_ERROR, log + " {{ " + tx + " }}");
				tx.printStackTrace(System.err);
			}
			@Override
			public void info(String log) {
				this.log(TYPE_INFO, log);
			}
			@Override
			public void log(int type, String log) {
				String logo = "";
				switch (type) {
				case TYPE_INFO:
					logo = "[SPIDER I]";
					break;
				case TYPE_DEBUG:
					logo = "[SPIDER D]";
					break;
				case TYPE_ERROR:
					logo = "[SPIDER E]";
					break;
				default:
					logo = "[SPIDER X]";
					break;
				}
				System.out.println(logo + "\t" + log);
			}}, null);
		return context;
	}

	private ILogger logger;
	private ISpiderHandler handler;
	
	public SpiderContext(ILogger logger, ISpiderHandler handler) {
		super();
		this.logger = logger;
		this.handler = handler;
	}

	public ILogger getLogger() {
		return logger;
	}
	
	public ISpiderHandler getHandler() {
		return handler;
	}
	
	
}
