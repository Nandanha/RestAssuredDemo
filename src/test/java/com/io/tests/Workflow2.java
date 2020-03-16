package com.io.tests;

import com.io.utils.CommonFuntionalities;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Workflow2 {
    public static String token ="";
    public static String ctoken ="";
    public static int masterid = 0;
    public static int clientid = 0;
    public static int userid = 0;
    public static int propertyid = 0;
    public static int propertyEquipmentid=0;
    public static int serviceContractid=0;
    public static int insuranceid = 0;
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

    @Test(priority = 1)
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

    @Test(priority = 2, dependsOnMethods = "postMasterEntity")
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

    @Test(priority = 3, dependsOnMethods = "getMasterID")
    public void postClientMasterID() throws IOException {
        payloadFilePath = new File(".\\src\\test\\payload\\clientMasterNew.txt");
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


    @Test(priority = 4, dependsOnMethods = "postClientMasterID")
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

    @Test(priority = 5, dependsOnMethods = "getClientMasterID")
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

    @Test(priority = 6, dependsOnMethods = "postUserID")
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

    @Test(priority =7,dependsOnMethods = "getUserID")
    public void postNewUserLoginTest() throws IOException {
        payload = "{\n" +
                "\t\"username\":\"iuser"+clientid+"\",\n" +
                "\t\"password\":\"admin\"\t\n" +
                "}";
        response = commonFuntionalities.postOperation_Nonadmin("/login", payload, token,"wayne_admin","admin");
        jsonPathValidator = response.jsonPath();
        System.out.println("-------------------+status code:"+response.getStatusCode());
        Assert.assertTrue(response.getStatusCode()==200);
        ctoken = "Bearer " + jsonPathValidator.get("data.token");
        System.out.println("updated: "+ctoken);
    }

    @Test(priority = 8, dependsOnMethods = "getClientMasterID")
    public void postProperty() throws IOException {
        payloadFilePath = new File(".\\src\\test\\payload\\One WTC.txt");
        String payload = FileUtils.readFileToString(payloadFilePath);
        String exactpayload=  payload.replace("\"primaryContact\":1","\"primaryContact\":"+String.valueOf(clientid));
        System.out.println("payload after:"+exactpayload);
        response = commonFuntionalities.postOperation("/property", exactpayload, ctoken);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        propertyid = jsonPathValidator.get("data.propertyId");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        System.out.println("id : \n" + jsonPathValidator.get("data.propertyId"));
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));

        /*
        payloadFilePath = new File(".\\src\\test\\payload\\updateProperty.txt");
        String putpayload = FileUtils.readFileToString(payloadFilePath);
        String primcontact=  putpayload.replace("\"primaryContact\":7","\"primaryContact\":"+String.valueOf(clientid));
        String propid=  primcontact.replace("\"propertyId\":3","\"propertyId\":"+String.valueOf(propertyid));
        String changedPayload=  propid.replace("\"comments\":\"New building\"","\"comments\":\"update building\"");
        response = commonFuntionalities.putOperation("/property/"+String.valueOf(propertyid)+"?entityId="+ String.valueOf(masterid) ,changedPayload,token);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
*/

    }

    @Test(priority = 9, dependsOnMethods = "postProperty")
    public void getproperty() throws IOException {
        response = commonFuntionalities.getOperation("/property/" + String.valueOf(propertyid), ctoken);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        int id = jsonPathValidator.get("data.propertyId");
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
        Assert.assertTrue(String.valueOf(id).equals(String.valueOf(propertyid)));

    }


 //   @Test(priority = 10 ,dependsOnMethods ="postProperty")
    public void postServiceContract() throws IOException {
        payloadFilePath = new File(".\\src\\test\\payload\\serviceContractFormData.txt");
        payload = FileUtils.readFileToString(payloadFilePath);
        System.out.println("payload before:"+payload);
        String exactpayload=  payload.replace("\"propertyId\":1","\"propertyId\":"+String.valueOf(propertyid));
        System.out.println("payload after:"+exactpayload);
        response = commonFuntionalities.postFormOperation("/serviceContract",exactpayload,ctoken,"contractData");
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        serviceContractid = jsonPathValidator.get("data.serviceContractId");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        System.out.println("id : \n" + jsonPathValidator.get("data.serviceContractId"));
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
/*
        String updatedpayload=  exactpayload.replace("\"comments\":\"new service contract ACME\"","\"comments\":\"updated comment\"");
        response = commonFuntionalities.putFormOperation("/serviceContract/"+String.valueOf(serviceContractid)+"?entityId="+ String.valueOf(masterid) ,updatedpayload,token,"contractData");
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));

*/
    }


   // @Test(priority = 11, dependsOnMethods = "postServiceContract")
    public void getServiceContract() throws IOException {
        response = commonFuntionalities.getOperation("/serviceContract/" + String.valueOf(serviceContractid) , ctoken);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        int id = jsonPathValidator.get("data.serviceContractId");
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
        Assert.assertTrue(String.valueOf(id).equals(String.valueOf(serviceContractid)));

    }

    //@Test(priority = 12 , dependsOnMethods = "postProperty")
    public void postInsuraceForProperty() throws IOException {
        payloadFilePath = new File(".\\src\\test\\payload\\Insurance.txt");
        payload = FileUtils.readFileToString(payloadFilePath);
        String updatepayload=payload.replace("\"propertyId\":1","\"propertyId\":"+String.valueOf(propertyid));
        String exactpayload=updatepayload.replace("\"broker\":1","\"broker\":"+String.valueOf(clientid));
        response = commonFuntionalities.postFormOperation("/insurancePolicy",exactpayload,ctoken,"insuranceData");
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        insuranceid = jsonPathValidator.get("data.insurancePolicyId");
        System.out.println("status : \n" + jsonPathValidator.get("status"));
        System.out.println("id : \n" + jsonPathValidator.get("data.insurancePolicyId"));
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
/*
        String changedPayload=  exactpayload.replace("\"notes\":\"new insurance ACME\"","\"notes\":\"Updated note\"");
        response = commonFuntionalities.putFormOperation("/serviceContract/"+String.valueOf(insuranceid)+"?entityId="+ String.valueOf(masterid) ,changedPayload,token,"insuranceData");
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
*/
    }


    //@Test(priority = 13, dependsOnMethods = "postInsuraceForProperty")
    public void getInsurance() throws IOException {
        response = commonFuntionalities.getOperation("/insurancePolicy/" + String.valueOf(insuranceid) , ctoken);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        int id = jsonPathValidator.get("data.insurancePolicyId");
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
        Assert.assertTrue(String.valueOf(id).equals(String.valueOf(insuranceid)));

    }

    //@Test(priority = 14, dependsOnMethods = "getInsurance")
    public void postPropertyEquipment() throws IOException {
        payloadFilePath = new File(".\\src\\test\\payload\\propertyEquipment.txt");
        String payload = FileUtils.readFileToString(payloadFilePath);
        String exactpayload=  payload.replace("\"propertyId\":1","\"propertyId\":\""+String.valueOf(propertyid)+"\"");
        String propertyEquipmentPayload=  exactpayload.replace("\"propertyEquipmentTag\":\"AH terrace\"","\"propertyEquipmentTag\":\"AH terrace"+String.valueOf(Math.random())+"\"");
        payloadFilePath = new File(".\\src\\test\\payload\\equipment.txt");
        payload = FileUtils.readFileToString(payloadFilePath);
        String equipmentPayload=  payload.replace("\"model\":\"TYN678\"","\"model\":\"TYN678"+String.valueOf(Math.random())+"\"");
        //String equipmentPayload=  payload.replace("\"model\":\"TYN678\"","\"model\":\"TYN6789");

        payloadFilePath = new File(".\\src\\test\\payload\\component.txt");
        payload = FileUtils.readFileToString(payloadFilePath);
        String componentpayload=payload;
        System.out.println("payload after:"+propertyEquipmentPayload);
        System.out.println("payload after:"+equipmentPayload);
        System.out.println("payload after:"+componentpayload);
        response = commonFuntionalities.postMultipleFormOperation("/propertyEquipment", propertyEquipmentPayload,equipmentPayload,componentpayload, ctoken,"propertyEquipment","equipment","components");
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        propertyEquipmentid = jsonPathValidator.get("data.savedPropertyEquipmentId");
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));

    }

    //@Test(priority = 15, dependsOnMethods = "postPropertyEquipment")
    public void getEquipmentMaster() throws IOException {
        response = commonFuntionalities.getOperation("/propertyEquipment/"+propertyEquipmentid+"?type=HVAC" , ctoken);
        jsonPathValidator = response.jsonPath();
        System.out.println("Response :" + response.asString());
        // Get specific element from JSON document
        status = jsonPathValidator.get("status");
        int id = jsonPathValidator.get("data.propertyEquipment.propertyEquipmentId");
        // Validate if the specific JSON element is equal to expected value
        Assert.assertTrue(status.equalsIgnoreCase("SUCCESS"));
        Assert.assertTrue(String.valueOf(id).equals(String.valueOf(propertyEquipmentid)));

    }

    //clean up operations are performed
    //@AfterClass
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