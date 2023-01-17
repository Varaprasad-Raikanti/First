package Utilities;

 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

 

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

 

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

 

public class xmlReader {
    
    public static void main(String[] args) throws Exception {
        
        
        String fileName="C:\\Users\\Mahendra\\Desktop\\Export_4000420945A.xml";
        
        Map<String, Object> jsonInMap=fetchvalues(fileName);
        
        System.out.println(jsonInMap.get("CardName"));
        
         List<String> keys = new ArrayList<String>(jsonInMap.keySet());
            for (String key : keys) {
            System.out.println(key + ": " + jsonInMap.get(key));
        
        
      
      //  Map<String,Object> map=stringToMap(jsonInMap.get(key).toString());
        
        
   //
        }
        System.out.println("========");
        System.out.println(jsonInMap.get("OrderItems1"));
    }

 

    public static Map<String,Object> stringToMap(String value)
    {
        Map<String,Object> values=new HashMap<String, Object>();
        value=value.replace("{", "").replace("}", "").replace("[", "").replace("]", "");
        
        System.out.println(value);
        for(int i=0;i<value.split(",").length;i++)
        {   
        	
        	
       
        values.put(value.split(",")[i].split("=")[0],value.split(",")[i].split("=")[1]);
       
        	
        
        }
        return values;
        
        
    }
    
    public static Map<String,Object> stringToMapTest(String value)
    {
        Map<String,Object> values=new HashMap<String, Object>();
        value=value.replace("{", "").replace("}", "").replace("[", "").replace("]", "");
        
      
        for(int i=0;i<value.split(",").length;i++)
        {   
        	
        	
        	String [] sid=value.split(",")[i].split("=");
        	
        	if(sid.length==2) {
        		
        		 values.put(value.split(",")[i].split("=")[0].trim(),value.split(",")[i].split("=")[1].trim());
        	}
        	else {
        		values.put(value.split(",")[i].split("=")[0].trim(),null);
        	}
       
       
        	
        	
        
        }
        
     
       
       
      
        return values;
        
        
    }
    
    public static Map<String, Object> fetchvalues(String fileName) throws IOException
    {
        InputStream is = new FileInputStream(new File(fileName));
        String xml = IOUtils.toString(is).replace("&", "&amp;");

 

        XMLSerializer xmlSerializer = new XMLSerializer();
        JSON json = xmlSerializer.read(xml);

 

    //    System.out.println(json.toString(2));

 

        String jsonString=json.toString(2);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonInMap=null;
        try {
        jsonInMap = mapper.readValue(jsonString,
        new TypeReference<Map<String, Object>>() {
        });
        
        List<String> keys = new ArrayList<String>(jsonInMap.keySet());
        for (String key : keys) {
        if(key.equalsIgnoreCase("OrderItems"))
        {
            String value=jsonInMap.get(key).toString();
            String test[]=value.split("\\{");
            for(int i=1;i<value.split("\\{").length;i++)
            {
                jsonInMap.put("OrderItems"+i, value.split("\\{")[i].split("\\}")[0]);
            }
            
        }}
        

 

        } catch (JsonGenerationException e) {
        e.printStackTrace();
        } catch (JsonMappingException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        }
        return jsonInMap;
        }
        

 

}
 