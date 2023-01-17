package APITests;

import static org.hamcrest.Matchers.notNullValue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import TestLib.Automation_properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class RestTest{
	
	public static List<String> list = new ArrayList<String>();
	
	String restAPIURL;
	public RestTest() throws FileNotFoundException, IOException 
	{
		restAPIURL=Automation_properties.getInstance().getProperty(Automation_properties.BASEURL);
		System.out.println(restAPIURL);
	}
	
	@Test
	public Response restPost(String url,String TestDatafilePath) throws IOException
	{
		restAPIURL=restAPIURL+url;
		FileInputStream inputSrc = new FileInputStream(System.getProperty("user.dir")+TestDatafilePath);
	    String apiBody = IOUtils.toString(inputSrc);
		RequestSpecBuilder builder=new RequestSpecBuilder();
		
		//Header
		builder.setContentType("Content-Type; text/plain")
		.addHeader("Content-Length", "0")
		.addHeader("Cache-Control", "no-cache");
		
		
		//Request Body
		builder.setBody(apiBody);
		
		RequestSpecification requestSpc=builder.build();
		
		Response response= RestAssured.given().authentication().basic("","").contentType("application/json; charset=utf-8").body(apiBody).post(restAPIURL);
		
		System.out.println(response.getStatusCode());
				
		return response;
		
		
		
	}
	
	public  Response restGet( String url) {
		System.out.println(restAPIURL+url);
		return RestAssured.given().headers("", "").contentType("application/json; charset=utf-8").expect().body(notNullValue()).when().get(restAPIURL+url);

	}
	
	
	public String getValueFromJson(Response response,String Key)
	{
		JsonObject JSONResponse=new JsonObject().get(response.body().asString()).getAsJsonObject();
		return JSONResponse.get(Key).getAsString();
	}
	
	public void sucessStatusValidation(Response response)
	{
		Assert.assertEquals(200, response.getStatusCode());
	}

	
	public static List<String> getValue(String key,String Json)
	{
		JsonParser p = new JsonParser();
        check(key, p.parse(Json));
        System.out.println(list);
        return list;
	}
	private static void check(String key, JsonElement jsonElement) {
		
        if (jsonElement.isJsonArray()) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
                check(key, jsonElement1);
            }
        } else {
            if (jsonElement.isJsonObject()) {
                Set<Map.Entry<String, JsonElement>> entrySet = jsonElement
                        .getAsJsonObject().entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet) {
                    String key1 = entry.getKey();
                    if (key1.equals(key)) {
                        list.add(entry.getValue().toString());
                    }
                    check(key, entry.getValue());
                }
            } else {
                if (jsonElement.toString().equals(key)) {
                    list.add(jsonElement.toString());
                }
            }
        }
    }

	
}