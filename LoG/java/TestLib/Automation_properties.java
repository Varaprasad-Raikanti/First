package TestLib;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Automation_properties {

	
	private static String configFile = System.getProperty("configFile","config.properties");
	public static String testNG =  System.getProperty("user.dir")+"\\src\\test\\resources\\TestNGfiles\\"+System.getProperty("testNG","TestNG.xml");
	private static Properties automation_properties = new Properties();

	
	public final static String BASEURL = "BASEURL";
	public final static String BROWSER = "BROWSER";
	public final static String CHROMEVERSION = "CHROMEVERSION";
	public final static String UNAME = "UNAME";
	public final static String PWD = "PWD";
	public final static String CONN_STR = "CONN_STR";
	public final static String SQLJAVADriver = "SQLJAVADriver";
	public final static String TEST_DATA = "TEST_DATA";
	public final static String Exestartmail = "Exestartmail";
	public final static String Exemail = "Exemail";
	public final static String waitTime = "waitTime";
	public final static String WEBSITE = "WEBSITE";
	public final static String projPATH = "projPATH";
	public static final String NoOfSessions = "NoOfSessions";
	public static final String USERNAME = "USERNAME";
	public static final String PASSWORD = "PASSWORD";
	private static Automation_properties instance;
	public static String RunningBrowser = "GC";
	public final static String NEWCOMPARISON = "NEWCOMPARISON";
	public static final String EncryptPassword = "EncryptPassword";
	public static final String FAAS = "FAASFLAG";
	public final static String VideoRecord = "VideoRecord";
	public final static String VIEW = "VIEW";
	public final static String DeviceName = "DeviceName";
	public final static String UDID = "UDID";
	public final static String PlatformVersion = "PlatformVersion";

	public static void setTestNG(String testNGFile) {
		testNG = testNGFile;
	}

	public static String getTestNGFilePath(){
		File testNGFile = new File(testNG);
		if (testNGFile.exists()) {
			return testNGFile.getAbsolutePath();
		}
		Driver.getLogger().info(System.getProperty("user.dir") + "/" + testNG);
		return System.getProperty("user.dir") + "/" + testNG;
	}

	public static String getHostName() {
		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			return addr.getHostName();
		} catch (UnknownHostException ex) {
			System.out.println("Hostname can not be resolved");
		}
		return "UnKnown";
	}

	// Call this method only to change the file from which the properties have
	// to be loaded.
	private Automation_properties() {
		System.out.println("Loading automation properties from the file" + System.getProperty("configFile",configFile));
		loadProperties();
	}

	private static void createInstance() {
		instance = new Automation_properties();
	}

	public static void createInstance(String configFile) {
		Automation_properties.configFile = configFile;
		if (instance == null) {
			createInstance();
		} else {
			System.out.println("Reloading automation properties from the file " + configFile);
			loadProperties();
		}
	}

	public static String getConfigFile() {
		return configFile;
	}

	private static void loadProperties() {
		try {
			automation_properties.clear();	
			System.out.println( System.getProperty("configFile",configFile));
			automation_properties.load(new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\Config\\" + System.getProperty("configFile",configFile)));
			automation_properties.setProperty("ReportEmail",System.getProperty("mailId","mchiruvella@gmail.com"));
			//automation_properties.setProperty("BASEURL",System.getProperty("BASEURL",BASEURL));
			//automation_properties.setProperty("BROWSER",System.getProperty("browser",BROWSER));
			automation_properties.setProperty("WEBSITE",System.getProperty("WEBSITE","Revlon"));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load the properties:"+e);
		}

		assert !automation_properties.isEmpty();
	}

	// Always use the getInstance method, you can't create your own object, only replace the config file using
	// createInstance(configFile).
	public static Automation_properties getInstance() {

		if (instance == null) {
			createInstance();
		}
		return instance;
	}
	
	public static void setInstance(Automation_properties prop) {
		instance=prop;
		
	}

	/**
	 * returns the value of the property denoted by key
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(final String key) {
		String property = automation_properties.getProperty(key);
		return property != null ? property.trim() : property;
	}

	public void setProperty(String key, String value) {
		automation_properties.setProperty(key, value);
	}

	public boolean isProperty(final String key) {
		String property = automation_properties.getProperty(key);
		if (property != null)
			return true;
		else
			return false;

	}

	public int getNumOfProperties() {
		return automation_properties.size();
	}

}
