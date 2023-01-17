package Utilities;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import TestLib.Automation_properties;
import TestLib.Driver;
import TestNg.reader.Classes;
import TestNg.reader.Listener;
import TestNg.reader.Listeners;
import TestNg.reader.Parameter;
import TestNg.reader.Suite;
import TestNg.reader.Test;


public class GenerateFailedTestNG implements Closeable{
	private HashMap<String,String> testIds=new HashMap<>();
	private HashMap<String,String> priorities=new HashMap<>();
	private HashMap<String,String> descriptions=new HashMap<>();
	private HashMap<String,String> testNames=new HashMap<>();
	private List<String> testCasesToRerun = new ArrayList<>();
	private final String failedTestNGFileName = "FailedTestNG.xml";
	private final String listenerFileName = "HA.Utilities.Listener";
	private int threadCount;
	GenerateFailedTestNG(int threadCount){
		try {
			ReadTestNGXMLFile testNGFile=new ReadTestNGXMLFile(Automation_properties.getTestNGFilePath());
			testIds=testNGFile.getAttributes("test", "testid");
			priorities=testNGFile.getAttributes("test", "priority");
			descriptions=testNGFile.getAttributes("test", "description");
			testNames = testNGFile.getAttributes("test", "name");
			this.threadCount = threadCount;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addTestCase(String name) {
		if(!testCasesToRerun.contains(name))
			testCasesToRerun.add(name);
	}

	@Override
	public void close() throws IOException {
		Suite suite = new Suite();
		suite.setName("ReRun");
		suite.setParallel("tests");
		suite.setThreadCount(Integer.toString(threadCount));
		Listeners listeners = new Listeners();
		Listener listener = new Listener();
		listener.setClassName(listenerFileName);
		listeners.getListener().add(listener);
		Parameter param = new Parameter();
		param.setName("configFile");
		param.setValue("${property}");
		suite.getListenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles().add(listeners);
		suite.getListenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles().add(param);
		for (int i = 0; i < testCasesToRerun.size(); i++) {
			String array[]=testCasesToRerun.get(i).split("\\.");
			String eachCaseName = array[array.length-1];
			String testName = testNames.get(eachCaseName);
			String testDescription = descriptions.get(eachCaseName);
			String priority = priorities.get(eachCaseName);
			String testID = testIds.get(eachCaseName);
			String className = testCasesToRerun.get(i);
			Test eachTest = new Test();
			eachTest.setTestid(testID);
			eachTest.setName(testName);
			eachTest.setDescription(testDescription);
			eachTest.setPriority(priority);
			Classes eachClasses = new Classes();
			TestNg.reader.Class eachClass = new TestNg.reader.Class();
			eachClass.setName(className);
			eachClasses.getClazz().add(eachClass);
			eachTest.setClasses(eachClasses);
			suite.getListenersOrPackagesOrTestOrParameterOrMethodSelectorsOrSuiteFiles().add(eachTest);
		}
		
		try {
			JAXBContext context = JAXBContext.newInstance(Suite.class);
			Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	        m.marshal(suite, new File(failedTestNGFileName));
		} catch (JAXBException e) {
			Driver.getLogger().info("Error while marshalling jaxb object to xml file", e);
		}
        
	}
	
	

}
