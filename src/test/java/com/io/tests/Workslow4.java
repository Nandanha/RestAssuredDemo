package com.io.tests;

import com.io.utils.CommonFuntionalities;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.approvaltests.Approvals;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import org.testng.Assert;


public class Workflow4 {
    public CommonFuntionalities commonFuntionalities = new CommonFuntionalities();
    public JsonPath jsonPathValidator = null;
    public File payloadFilePath = null;
    public String payload = "";
    Response response = null;

    @Test(priority = 1,description = "Create User and Validate")
    @Epic("REST API Automation Suite")
    @Feature("User creation test using POST")
    @Story("Payload for User creation is read and user is created using POST operation")
    public void postUserCreation() throws IOException {

      //  payloadFilePath = new File("/Users/nandan.anantharamu/IdeaProjects/RestAssuredDemo/src/test/payload/createUser.txt");
      //  payload = FileUtils.readFileToString(payloadFilePath);
        payload="{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"leader\"\n" +
                "}";
        response = commonFuntionalities.postOperationDemo("/api/users/", payload);
        jsonPathValidator = response.jsonPath();
        int status = response.getStatusCode();

        System.out.println("Response : " + response.asString());
        System.out.println("name : " + jsonPathValidator.get("name"));
        System.out.println("job : " + jsonPathValidator.get("job"));
        System.out.println("id : " + jsonPathValidator.get("id"));
        System.out.println("createdAt: " + jsonPathValidator.get("createdAt"));

        String responseStr = response.headers().toString() + "\n\n" + response.body().prettyPrint();
        responseStr = responseStr.replaceAll("Date=.*", "#####DATE");
        responseStr = responseStr.replaceAll("Set-Cookie=.*", "#####COOKIE");
        responseStr = responseStr.replaceAll("CF-RAY=.*", "#####CF-RAY");
        responseStr = responseStr.replaceAll("Etag=.*", "#####ETAG");
        responseStr = responseStr.replaceAll("\"id\": .*", "#####ID");
        responseStr = responseStr.replaceAll("\"createdAt\":.*", "#####CRETED_DATE_TIME_STAMP");

        Assert.assertEquals(status,201);
        Assert.assertTrue(jsonPathValidator.get("name").equals("morpheus"));
        Assert.assertTrue(jsonPathValidator.get("job").equals("leader"));
        Approvals.verify(responseStr);

    }
/*
    @Test(priority = 2,description = "GET operation to display details of specific User",dependsOnMethods = "postUserCreation")
    @Epic("REST API Automation Suite")
    @Feature("Check for a specific User details test")
    @Story("GET operation to read details of a specific user")
    public void getUserCreated() throws IOException {
        response = commonFuntionalities.getOperationDemo("/api/users/2");
        jsonPathValidator = response.jsonPath();
        int status = response.getStatusCode();

        System.out.println("Response :" + response.asString());
        System.out.println("id : " + jsonPathValidator.get("data.id"));
        System.out.println("email : " + jsonPathValidator.get("data.email"));
        System.out.println("first_name : " + jsonPathValidator.get("data.first_name"));
        System.out.println("last_name : " + jsonPathValidator.get("data.last_name"));

        Assert.assertEquals(status,200);
        Assert.assertTrue(jsonPathValidator.get("data.id").equals(2));
        Assert.assertTrue(jsonPathValidator.get("data.email").equals("janet.weaver@reqres.in"));
        Assert.assertTrue(jsonPathValidator.get("data.first_name").equals("Janet"));
        Assert.assertTrue(jsonPathValidator.get("data.last_name").equals("Weaver"));
    }

    @Test(priority = 3,description = "Put operation to update the details of a specific user",dependsOnMethods = "getUserCreated")
    @Epic("REST API Automation Suite")
    @Feature("Update previously created test")
    @Story("Payload for user previously created using PUT operation")
    public void putUserCreated() throws IOException {
      //  payloadFilePath = new File("/Users/nandan.anantharamu/IdeaProjects/RestAssuredDemo/src/test/payload/updateUser.txt");
      //  payload = FileUtils.readFileToString(payloadFilePath);
        payload="{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";
        response = commonFuntionalities.putOperationDemo("/api/users/2", payload);
        jsonPathValidator = response.jsonPath();
        int status = response.getStatusCode();

        System.out.println("Response :" + response.asString());
        System.out.println("name : " + jsonPathValidator.get("name"));
        System.out.println("job : " + jsonPathValidator.get("job"));
        System.out.println("updatedAt : " + jsonPathValidator.get("updatedAt"));

        Assert.assertEquals(status,200);
        Assert.assertTrue(jsonPathValidator.get("name").equals("morpheus"));
        Assert.assertTrue(jsonPathValidator.get("job").equals("zion resident"));
    }

    @Test(priority = 4,description = "DELETE operation to remove a previously created user",dependsOnMethods = "putUserCreated")
    @Epic("REST API Automation Suite")
    @Feature("Delete user feature test")
    @Story("Previously created user is deleted using DELETE operation")
    public void deleteUserCreated() throws IOException {
        response = commonFuntionalities.deleteOperationDemo("/api/users/2");
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        int status = response.getStatusCode();
        Assert.assertEquals(status,204);
    }

 */
}

