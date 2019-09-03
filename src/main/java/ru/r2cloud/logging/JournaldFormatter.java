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

	private final Date dat = new Date();

	@Override
	public synchronized String format(LogRecord record) {
		dat.setTime(record.getMillis());
		String source;
		if (record.getSourceClassName() != null) {
			source = record.getSourceClassName();
			if (record.getSourceMethodName() != null) {
				source += " " + record.getSourceMethodName();
			}
		} else {
			source = record.getLoggerName();
		}
		String message = formatMessage(record);
		String throwable = "";
		if (record.getThrown() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println();
			record.getThrown().printStackTrace(pw);
			pw.close();
			throwable = sw.toString();
		}
		return String.format(format, dat, source, record.getLoggerName(), convertLevelToSyslog(record.getLevel()), message, throwable);
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
