package embedded.mas.log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import jason.infra.local.RunLocalMAS;

public class EMasLogger {

	private Logger logger = Logger.getLogger("emasLogger");
	private static EMasLogger instance;
	
	
	private EMasLogger(){
		if(RunLocalMAS.getRunner() == null){ // it is not running under jason/jacamo
			logger.setLevel(Level.FINEST);
			try {
				logger.setUseParentHandlers(false);
				FileHandler handler = new FileHandler("emas.log");
				//SimpleFormatter formatter = new SimpleFormatter();
				handler.setFormatter(new EMasFormatter()); //Set log to plain text. Default (by Java) is xml
				logger.addHandler(handler);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.log(Level.FINE, "running on jacamo, using its logger setup.");
		}
	}
	
	public static EMasLogger getInstance(){
		if(instance==null){
			instance = new EMasLogger();		
		}
		return instance;
	}

	public void fine(String msg){
		logger.fine(msg);
	}
	
	public void info(String msg){
		logger.info(msg);
	}
	
}
