package com.pengjun.ka.utils;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class LoggerUtils {

	public static final Logger dbLogger = Logger.getLogger("db");
	public static final Logger serviceLogger = Logger.getLogger("service");
	public static final Logger kaLogger = Logger.getLogger("ka");
	public static final Logger clientLogger = Logger.getLogger("clientLogger");

	public static void initLogger(boolean useLogCatAppender, boolean useFileAppender) {
		LogConfigurator logConfigurator = new LogConfigurator();
		logConfigurator.setFileName(KaConstants.LOG_ROOT + File.separator + "ka.log");
		logConfigurator.setRootLevel(Level.DEBUG);
		logConfigurator.setLevel("org.apache", Level.ERROR);
		logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
		logConfigurator.setMaxFileSize(1024 * 1024 * 5);
		logConfigurator.setImmediateFlush(true);

		logConfigurator.setUseLogCatAppender(useLogCatAppender);
		logConfigurator.setUseFileAppender(useFileAppender);
		logConfigurator.configure();
		Logger logger = Logger.getLogger("ka");
		logger.info("ka logger start");
	}

}
