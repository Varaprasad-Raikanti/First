package Utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropertiesReader {

	public void properUpdate(String fileName,String key,String value)
	{
	try {
		
	      FileInputStream   fis = new FileInputStream(fileName);
	      Properties  prop = new Properties();
	     prop.load(fis);
	     

       

        // set the properties value
        prop.setProperty(key,value);
       

        // save properties to project root folder
        FileOutputStream output = new FileOutputStream(fileName,true);
        prop.store(output,null);

        System.out.println(prop);

    } catch (Exception io) {
        io.printStackTrace();
    }
	}


public static void main(String[] args) {
	PropertiesReader prop=new PropertiesReader();
	prop.properUpdate("config.properties","Order1","200000");
	prop.properUpdate("config.properties","Order2","300000");
	
}

}

