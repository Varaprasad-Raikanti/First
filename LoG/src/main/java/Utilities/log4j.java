package Utilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import TestLib.Common;

import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class log4j {
	
	static Logger log;
	
	/*public log4j()
	{
		log=Logger.getLogger(log4j1.class);
	}*/
	
    public log4j() {
        try {
            
        	String 	logFilepath = System.getProperty("user.dir")+"//TestLogs//logs//"+"Automation-Log-"+Utils.getDateTime()+".log";
            Files.deleteIfExists(new java.io.File(logFilepath).toPath());
            Properties propertie=new Properties();
            propertie.load(new FileInputStream("Log4j.properties"));
            propertie.setProperty("log4j.appender.file.File",logFilepath);
            propertie.store(new FileOutputStream("log4j.properties"), null);
        }catch (Exception e){}
        log = Logger.getLogger(log4j.class.getName());
        PropertyConfigurator.configure("Log4j.properties");
    }

    public static void info(Object infoMessage) {
    	info(String.valueOf(infoMessage));
    }
    
    
    public static void info(String infoMessage) {
        StackTraceElement l = new Exception().getStackTrace()[1];
        String className=l.getClassName().substring(l.getClassName().lastIndexOf('.')+1);
        String callerDetails=className+"-"+l.getMethodName()+"-LineNo:"+l.getLineNumber()+"-";
        if("Listener".equals(className))
        {
            log.info(infoMessage);
        }
        else
            {
            log.info(callerDetails+infoMessage);
            }
    }

    public static void error(String errorMessage) {
        StackTraceElement l = new Exception().getStackTrace()[1];
        String className=l.getClassName().substring(l.getClassName().lastIndexOf('.')+1);
        String callerDetails=className+"-"+l.getMethodName()+"-LineNo:"+l.getLineNumber()+"-";
        if("Report".equals(className))
        {
            log.info(errorMessage);
        }
        else
        {
            log.info(callerDetails+errorMessage);
        }
    }
    public static void warn(String warnMessage)
    {
        log.warn(warnMessage);
    }
    public static void fatal(String fatalMessage) {
    	log.fatal(fatalMessage);
    }

    public static void debug(String debugMessage) {
    	log.debug(debugMessage);
    }

	
	
	public static void info(int message)
	{
		info(String.valueOf(message));
	}
	
	public static void info(String message, Object o)
	{
		StackTraceElement l = new Exception().getStackTrace()[1];
        String className=l.getClassName().substring(l.getClassName().lastIndexOf('.')+1);
        String callerDetails=className+"-"+l.getMethodName()+"-LineNo:"+l.getLineNumber()+"-";
        if("Listener".equals(className))
        {
        	log.info(message+"---"+o);
        }
        else
            {
        	log.info(message+"---"+o);
            }
    }
	public static void info(Object message, String Data)
	{
			
		info(String.valueOf(message)+"-"+Data);
	}
	
	
	public static void info(String message, String Data)
	{
			
		info(message+"-"+Data);
	}
		
	
	
	public static void error(Object message)
	{
		log.error(message);
	}

}
