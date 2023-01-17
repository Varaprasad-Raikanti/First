package Utilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import TestLib.Driver;


public class ReadTestNGXMLFile {

	Document doc;

	public ReadTestNGXMLFile(String path) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Driver.getLogger().error("Error while reading the testng.xml file at location {}."+ e);
			Assert.fail("Error while reading the testng.xml file at location " + path);
		}
		
		doc.getDocumentElement().normalize();
	}

	public String getAllTestCaseIds() {
		String tcIDs = null;
		NodeList nList = doc.getElementsByTagName("test");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				if(tcIDs!=null)
					tcIDs = tcIDs + ",";
				tcIDs = tcIDs+eElement.getAttribute("testid");
			}
		}
		return tcIDs;
	}

	public String getModuleName() {
		String module = null;
		NodeList nList = doc.getElementsByTagName("class");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				module = eElement.getAttribute("name").split("\\.")[2];
			}
		}
		System.out.println("Module Name: "+module);
		return module;
	}

	public Set<String> getAllTestCaseNames() {
		Set<String> names = new HashSet<String>();
		NodeList nList = doc.getElementsByTagName("test");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				names.add(eElement.getAttribute("name"));
			}
		}
		return names;
	}
	
	public Set<String> getAllQualifiedClassNames() {
		Set<String> names = new HashSet<String>();
		NodeList nList = doc.getElementsByTagName("test");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				NodeList nListClass = doc.getElementsByTagName("class");
				Node nodeClass = nListClass.item(i);
				if (nodeClass.getNodeType() == Node.ELEMENT_NODE) {
					Element eElementClass = (Element) nodeClass;
					names.add(eElementClass.getAttribute("name"));
				}
			}
		}
		return names;
	}

	public void getAllTestCaseDescriptions() {
		NodeList nList = doc.getElementsByTagName("test");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				System.out.println(eElement.getAttribute("description"));
			}
		}
	}

	public void getSuiteName() {
		NodeList nList = doc.getElementsByTagName("suite");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				System.out.println(eElement.getAttribute("name"));
			}
		}
	}

	public String getParllelEqualTo() {
		NodeList nList = doc.getElementsByTagName("suite");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				try{
					return eElement.getAttribute("parallel");
				}
				catch(Exception e)
				{
					return null;
				}
			}
		}
		return null;
	}

	public int getThreadCount() {
		int count = 1;
		try {
			NodeList nList = doc.getElementsByTagName("suite");
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					count =Integer.parseInt(eElement.getAttribute("thread-count"));
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return count;
	}

	public String test(String TCName) throws Exception{
		HashMap<String, String> tname = new HashMap<>();
		String testName = "";
		String idDes = "";

		NodeList nList = doc.getElementsByTagName("test");
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				testName = eElement.getAttribute("name");
				idDes = eElement.getAttribute("description") +" "+ eElement.getAttribute("testid")+" "+eElement.getAttribute("priority");
				tname.put(testName, idDes);
			}
		}
		return tname.get(TCName);
	}

	public String getTestIDAndDesc(String TCName) throws Exception{
		HashMap<String, String> tname = new HashMap<>();
		String testName = "";
		String idDes = "";
		try {
			NodeList nList = doc.getElementsByTagName("test");
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					NodeList nListClass = doc.getElementsByTagName("class");
					Node nodeClass = nListClass.item(i);
					if (nodeClass.getNodeType() == Node.ELEMENT_NODE) {
						Element eElementClass = (Element) nodeClass;
						String tcName[] = eElementClass.getAttribute("name").split("\\.");
						testName = tcName[tcName.length-1];
					}
					idDes = eElement.getAttribute("description") +"||@@||"+ eElement.getAttribute("testid");
					tname.put(testName, idDes);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error in getting class name");
			e.printStackTrace();
			Assert.fail();
		}
		return tname.get(TCName);
	}
	
	public String getAttributesInTestTag(String TCName, String attributeName) throws Exception{
		HashMap<String, String> tname = new HashMap<>();
		String testName = "";
		String value = "";
		try {
			NodeList nList = doc.getElementsByTagName("test");
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					NodeList nListClass = doc.getElementsByTagName("class");
					Node nodeClass = nListClass.item(i);
					if (nodeClass.getNodeType() == Node.ELEMENT_NODE) {
						Element eElementClass = (Element) nodeClass;
						String tcName[] = eElementClass.getAttribute("name").split("\\.");
						testName = tcName[tcName.length-1];
					}
					value = eElement.getAttribute(attributeName);
					tname.put(testName, value);
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error in getting class name");
			e.printStackTrace();
			Assert.fail();
		}
		return tname.get(TCName);
	}

	public boolean isKarateTest(String TCName) throws Exception{
		String testName;
		try {
			NodeList nList = doc.getElementsByTagName("test");
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					NodeList nListClass = doc.getElementsByTagName("class");
					Node nodeClass = nListClass.item(i);
					if (nodeClass.getNodeType() == Node.ELEMENT_NODE) {
						Element eElementClass = (Element) nodeClass;
						String tcName[] = eElementClass.getAttribute("name").split("\\.");
						testName = tcName[tcName.length-1];
						if(testName.equals(TCName)){
							if(eElement.getAttribute("karate").equalsIgnoreCase("true"))
								return true;
							else return false;
						}
					}
				}
			}
		}
		catch(Exception e) {
			System.out.println("Error in checking whether a test is karate");
			e.printStackTrace();
			Assert.fail();
		}
		return false;
	}

	public HashMap<String,String> getAttributes(String tagName, String attributeName) throws Exception
	{
		HashMap<String, String> values = new HashMap<>();
		NodeList nList = doc.getElementsByTagName(tagName);
		for (int i = 0; i < nList.getLength(); i++) {
			Node node = nList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
//				System.out.println(eElement.getAttribute(attributeName));
				values.put(eElement.getAttribute("name"),eElement.getAttribute(attributeName));
			}
		}
		return values;
	}

	

}
