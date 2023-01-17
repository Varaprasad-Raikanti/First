package Utilities;


import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import static com.jayway.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

public class RESTHelper {

	private static String JIRAUser="manoj@lotuswave.net";
	private static String JIRAAPIKey="GsjBUhxsQT4Sf2qbvICi0BB2"; 
	private static String JIRAURI="https://helenoftroy.atlassian.net/rest/api/3/issue/";

	public static void updateJIRAStatus(String TestId, String TestStatus) {		
		
		List<String> TestIds = Arrays.asList(TestId.split(",", -1));

		for (String testId : TestIds) {
			String validRequest="{\n    \"fields\": {\n        \"##Customfield##\": {\n            \"value\": \"##Status##\"\n        }         \n    }\n}";
			if(testId.contains("BTY"))
				validRequest=validRequest.replace("##Customfield##", "customfield_11092").replace("##Status##",TestStatus);
			else if(testId.contains("OUS23"))
				validRequest=validRequest.replace("##Customfield##", "customfield_11100").replace("##Status##",TestStatus);
			else
				validRequest=validRequest.replace("##Customfield##", "customfield_11093").replace("##Status##",TestStatus);
			putRequest(validRequest,testId,TestStatus,204);
		}

	}


	public static boolean putRequest(String requestBody,String testID,String testStatus,int StatusCode) {
		Response response = given()
				.auth()
				.preemptive()
				.basic(JIRAUser,JIRAAPIKey)
				.header("Accept", ContentType.JSON.getAcceptHeader())
				.contentType(ContentType.JSON)
				.body(requestBody)
				.put(JIRAURI+testID)
				.then().extract().response();
		if(response.getStatusCode()==StatusCode) {
			System.out.println("JIRA Test Execution Status updated for "+testID+" as "+testStatus);
			return true;
		}
		else
			System.out.println("JIRA Test Execution Status not updated for "+testID);

		return false;
	}

}
