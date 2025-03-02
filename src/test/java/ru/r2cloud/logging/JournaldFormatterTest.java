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
	public void testSevere() {
		LogRecord log = createLogRecord(Level.SEVERE);
		assertEquals("<3>test message [TestLogger]\n", formatter.format(log));
	}

	@Test
	public void testInfo() {
		LogRecord log = createLogRecord(Level.INFO);
		assertEquals("<6>test message [TestLogger]\n", formatter.format(log));
	}

	@Test
	public void testWarning() {
		LogRecord log = createLogRecord(Level.WARNING);
		assertEquals("<4>test message [TestLogger]\n", formatter.format(log));
	}

	@Test
	public void testFiner() {
		LogRecord log = createLogRecord(Level.FINER);
		assertEquals("<7>test message [TestLogger]\n", formatter.format(log));
	}

	@Test
	public void testSource() {
		LogRecord log = createLogRecord(Level.SEVERE);
		log.setSourceClassName("TestClass");
		log.setSourceMethodName("testMethod");
		assertEquals("<3>test message [TestClass testMethod]\n", formatter.format(log));
	}

	@Test
	public void testSource2() {
		LogRecord log = createLogRecord(Level.SEVERE);
		log.setSourceClassName("TestClass");
		assertEquals("<3>test message [TestClass]\n", formatter.format(log));
	}

	@Test
	public void testThrowable() {
		LogRecord log = createLogRecord(Level.SEVERE);
		log.setThrown(new Exception("something went wrong"));
		String message = formatter.format(log);
		// @formatter:off
		String expected = "<3>test message [TestLogger]\n"
				+ "java.lang.Exception: something went wrong\n"
				+ "	at ru.r2cloud.logging.JournaldFormatterTest.testThrowable(JournaldFormatterTest.java:62)\n";
		// @formatter:on
		assertTrue(message.startsWith(expected));
		assertTrue(message.endsWith(")\n")); // no extra new line
	}

	private static LogRecord createLogRecord(Level level) {
		LogRecord result = new LogRecord(level, "test message");
		result.setMillis(1567537684956L);
		result.setLoggerName("TestLogger");
		return result;
	}

	@Before
	public void start() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Properties props = new Properties();
		props.put("java.util.logging.JournaldFormatter.format", "<%4$s>%5$s [%2$s]%6$s%n");
		props.store(baos, "no comments");
		LogManager.getLogManager().readConfiguration(new ByteArrayInputStream(baos.toByteArray()));
		formatter = new JournaldFormatter();
	}

}
