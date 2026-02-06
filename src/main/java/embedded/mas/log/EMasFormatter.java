package embedded.mas.log;

import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class EMasFormatter extends SimpleFormatter {
	
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	@Override
    public String format(final LogRecord r){
		 StringBuilder sb = new StringBuilder();
		 sb.append(new Date(r.getMillis())).append( "   " ) 
				   .append(r.getLevel()).append( "   " ) 
		           .append(r.getMessage())
		           .append(LINE_SEPARATOR);
		 
		 
		 return sb.toString();
	}

}
