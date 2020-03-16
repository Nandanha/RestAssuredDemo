package com.io.tests;

import com.io.listner.TestListener;
import com.io.utils.CommonFuntionalities;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Listeners(TestListener.class)

public class Workflow1 {
    public static String ctoken = "";
    public static String token = "";
    public static int masterid = 0;
    public static int clientid = 0;
    public static int userid = 0;
    public static String username = "";
    public static String password = "admin";
    public static Properties prop;
    public CommonFuntionalities commonFuntionalities = null;
    public Response response = null;
    public JsonPath jsonPathValidator = null;
    public String status = "";
    public File payloadFilePath = null;
    public String payload = "";

    @BeforeClass
    void initialisetest() throws IOException {
        commonFuntionalities = new CommonFuntionalities();
        payloadFilePath = new File(".\\src\\test\\payload\\tokenGeneration.txt");
        payload = FileUtils.readFileToString(payloadFilePath);
        response = commonFuntionalities.postOperation("/login", payload, token);
        jsonPathValidator = response.jsonPath();
        System.out.println("token : \n" + jsonPathValidator.get("data.token"));
        token = "Bearer " + jsonPathValidator.get("data.token");
        System.out.println("Updated token:" + token);
        prop = commonFuntionalities.loadConfig();
        prop.setProperty("token", token);
        System.out.println("Updated token:" + prop.getProperty("token"));
    }


    @Test(priority = 1,description = "Verifies master entity with post operation")
    @Epic("REST API Automation Suite")
    @Feature("Master Entity creation")
    @Story("Payload for master entity creation is read and MasterID is created using POST operation")
    @Step("Generating token for all the tests and MasterID generation")
    public void postMasterEntity() throws IOException {
        payloadFilePath = new File(".\\src\\test\\payload\\masterEntity.txt");
        payload = FileUtils.readFileToString(payloadFilePath);
        response = commonFuntionalities.postOperation("/entityMaster/", payload, token);
        jsonPathValidator = response.jsonPath();
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        System.out.println("id : \n" + jsonPathValidator.get("data.entityMasterId"));
        masterid = jsonPathValidator.get("data.entityMasterId");
        System.out.println("------masterid:" + masterid);
    }

    @Test(priority = 2, dependsOnMethods = "postMasterEntity",description = "verifies get operation of entity master")
    @Epic("REST API Automation Suite")
    @Feature("Master Entity verification")
    @Story("GET operation of MasterID created in previous Test")
    @Step("Asserting the Master entity")
    public void getMasterID() throws IOException {
        response = commonFuntionalities.getOperation("/entityMaster/" + masterid, token);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        int id = jsonPathValidator.get("data.entityId");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        System.out.println("id : \n" + jsonPathValidator.get("data.entityId"));

        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
        Assert.assertTrue(String.valueOf(id).equals(String.valueOf(masterid)));
    }


    @Test(priority = 3, dependsOnMethods = "getMasterID",description = "Creates client master using post operation")
    @Epic("REST API Automation Suite")
    @Feature("Client Master creation")
    @Story("POST operation to create clientmaster for master entity created")
    @Step("Creating client master")
    public void postClientMasterID() throws IOException {
        payloadFilePath = new File(".\\src\\test\\payload\\clientMaster.txt");
        payload = FileUtils.readFileToString(payloadFilePath);
        response = commonFuntionalities.postOperation("/clientMaster?entityId=" + String.valueOf(masterid), payload, token);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        int id = jsonPathValidator.get("data.clientMasterId");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        System.out.println("id : \n" + jsonPathValidator.get("data.clientMasterId"));
        clientid = jsonPathValidator.get("data.clientMasterId");
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
    }


    @Test(priority = 4, dependsOnMethods = "postClientMasterID",description = "verifies get operation for Client master")
    @Epic("REST API Automation Suite")
    @Feature("Client Master Entity verification")
    @Story("GET operation of client master created in previous Test")
    @Step("Asserting the client master entity")
    public void getClientMasterID() throws IOException {
        response = commonFuntionalities.getOperation("/clientMaster/" + String.valueOf(clientid) + "?entityId=" + String.valueOf(masterid), token);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        int id = jsonPathValidator.get("data.clientMasterId");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        // System.out.println("id : \n" + jsonPathValidator.get("data.entityId"));

        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
        Assert.assertTrue(String.valueOf(id).equals(String.valueOf(clientid)));
    }

    @Test(priority = 5, dependsOnMethods = "getClientMasterID",description = "creates user for client master entity")
    @Epic("REST API Automation Suite")
    @Feature("user creation for client master")
    @Story("POST operation of user creation for the client master created")
    @Step("User creation")
    public void postUserID() throws IOException {
        String payload = "{\n" +
                "\t\"clientMasterId\":" + clientid + ",\n" +
                "\t\"username\":\"iuser"+clientid+"\",\n" +
                "\t\"password\":\"admin\",\n" +
                "\t\"email\":\"admin@wayne.com\",\n" +
                "\t\"userType\": \"TECHNICIAN\",\n" +
                "\t\"role\":\"ADMIN\"\n" +
                "}";
        response = commonFuntionalities.postOperation("/user?entityId=" + String.valueOf(masterid), payload, token);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        userid = jsonPathValidator.get("data.userId");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        System.out.println("id : \n" + jsonPathValidator.get("data.userId"));

        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
    }

    @Test(priority = 6, dependsOnMethods = "postUserID",description = "verifies get operation of user id created ")
    @Epic("REST API Automation Suite")
    @Feature("User ID verification")
    @Story("GET operation of UserID created in previous Test")
    @Step("Asserting the User Id generated")
    public void getUserID() throws IOException {
        response = commonFuntionalities.getOperation("/user/" + String.valueOf(userid) + "?entityId=" + String.valueOf(masterid), token);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        int id = jsonPathValidator.get("data.userId");
        username = jsonPathValidator.get("data.username");
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
        Assert.assertTrue(String.valueOf(id).equals(String.valueOf(userid)));

    }

    @Test(priority = 7, dependsOnMethods = "getUserID",description = "verifies login functionality of User")
    @Epic("REST API Automation Suite")
    @Feature("User Creation verification")
    @Story("POST operation of USER created in previous Test")
    @Step("Asserting the USER credentials")
    public void postNewUserLoginTest() throws IOException {
        payload = "{\n" +
                "\t\"username\":\"iuser\",\n" +
                "\t\"password\":\"admin\"\t\n" +
                "}";
        response = commonFuntionalities.postOperation_Nonadmin("/login", payload, token,"wayne_admin","admin");
        jsonPathValidator = response.jsonPath();
        System.out.println("-------------------+status code:"+response.getStatusCode());
        Assert.assertTrue(response.getStatusCode()==200);
        System.out.println("token : \n" + jsonPathValidator.get("data.token"));
        ctoken = "Bearer " + jsonPathValidator.get("data.token");
        System.out.println("token:"+token);
    }


    //clean up operations are performed
    @AfterClass
    public void DeleteMasterdataAndUser() throws IOException {
        //removing the masterEntity
        response = commonFuntionalities.deleteOperation("/entityMaster/" + String.valueOf(masterid)+ "?entityId=" + String.valueOf(masterid), token);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
/*
        //removing the user created
        response = commonFuntionalities.deleteOperation("/user/" + String.valueOf(userid) + "?entityId=" + String.valueOf(masterid), token);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));

 */
    }

}