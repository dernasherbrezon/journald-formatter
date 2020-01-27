package ru.r2cloud.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import org.junit.Before;
import org.junit.Test;

public class JournaldFormatterTest {

	private JournaldFormatter formatter;

	@Test
	public void testSevere() throws Exception {
		LogRecord record = createLogRecord(Level.SEVERE);
		assertEquals("<3>test message [TestLogger]\n", formatter.format(record));
	}

	@Test
	public void testInfo() throws Exception {
		LogRecord record = createLogRecord(Level.INFO);
		assertEquals("<6>test message [TestLogger]\n", formatter.format(record));
	}

	@Test
	public void testWarning() throws Exception {
		LogRecord record = createLogRecord(Level.WARNING);
		assertEquals("<4>test message [TestLogger]\n", formatter.format(record));
	}

	@Test
	public void testFiner() throws Exception {
		LogRecord record = createLogRecord(Level.FINER);
		assertEquals("<7>test message [TestLogger]\n", formatter.format(record));
	}

	@Test
	public void testSource() throws Exception {
		LogRecord record = createLogRecord(Level.SEVERE);
		record.setSourceClassName("TestClass");
		record.setSourceMethodName("testMethod");
		assertEquals("<3>test message [TestClass testMethod]\n", formatter.format(record));
	}

	@Test
	public void testSource2() throws Exception {
		LogRecord record = createLogRecord(Level.SEVERE);
		record.setSourceClassName("TestClass");
		assertEquals("<3>test message [TestClass]\n", formatter.format(record));
	}

	@Test
	public void testThrowable() throws Exception {
		LogRecord record = createLogRecord(Level.SEVERE);
		record.setThrown(new Exception("something went wrong"));
		String message = formatter.format(record);
		assertTrue(message.startsWith("<3>test message\njava.lang.Exception: something went wrong\n\tat ru.r2cloud.logging.JournaldFormatterTest.testThrowable(JournaldFormatterTest.java:"));
	}

	private static LogRecord createLogRecord(Level level) {
		LogRecord record = new LogRecord(level, "test message");
		record.setMillis(1567537684956L);
		record.setLoggerName("TestLogger");
		return record;
	}

	@Before
	public void start() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Properties props = new Properties();
		props.put("java.util.logging.JournaldFormatter.format", "<%4$s>%5$s%6$s [%2$s]%n");
		props.store(baos, "no comments");
		LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(baos.toByteArray()));
		formatter = new JournaldFormatter();
	}

}
