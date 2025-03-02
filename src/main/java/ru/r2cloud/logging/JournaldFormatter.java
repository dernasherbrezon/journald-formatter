package ru.r2cloud.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class JournaldFormatter extends Formatter {

    private final String format = LogManager.getLogManager().getProperty("java.util.logging.JournaldFormatter.format");

	private final Date date = new Date();

	@Override
	public synchronized String format(LogRecord entry) {
		date.setTime(entry.getMillis());
		String source;
		if (entry.getSourceClassName() != null) {
			source = entry.getSourceClassName();
			if (entry.getSourceMethodName() != null) {
				source += " " + entry.getSourceMethodName();
			}
		} else {
			source = entry.getLoggerName();
		}
		String message = formatMessage(entry);
		String throwable = "";
		if (entry.getThrown() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			entry.getThrown().printStackTrace(pw);
			pw.close();
			throwable = sw.toString().trim(); // remove last \n
			throwable = "\n" + throwable;
		}
		return String.format(format, date, source, entry.getLoggerName(), convertLevelToSyslog(entry.getLevel()), message, throwable);
	}

	private static int convertLevelToSyslog(Level level) {
		if (level.equals(Level.WARNING)) {
			return 4;
		}
		if (level.equals(Level.SEVERE)) {
			return 3;
		}
		if (level.equals(Level.FINE) || level.equals(Level.FINER) || level.equals(Level.FINEST) || level.equals(Level.ALL)) {
			return 7;
		}
		// the rest is info
		return 6;
	}
}
