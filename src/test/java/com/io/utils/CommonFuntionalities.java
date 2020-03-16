package com.io.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

public class CommonFuntionalities {
    public static Response response = null;
    public static RequestSpecification request= null;
    public static Properties prop = new Properties();

    public Properties loadConfig() throws IOException {
        File conffile = new File(".\\src\\main\\resources\\config.properties");
        FileInputStream fios = new FileInputStream(conffile);
        prop.load(fios);
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        return prop;
    }

   public Response getOperation(String endpoint,String token) throws IOException {
        prop=loadConfig();
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        request=given().log().all()
                .auth().basic(prop.getProperty("superusername"), prop.getProperty("superpassword"))
                .header("Authorization",token)
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization",prop.getProperty("token"));
        response = request.get(BaseURL+endpoint);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());
        return response;
    }

    public Response postOperation(String endpoint,String payload,String token) throws IOException {
        prop=loadConfig();
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        String updatedurl=BaseURL+endpoint;
        System.out.println("--------------updatedurl:"+updatedurl);
        request=given().log().all()
                .auth().basic(prop.getProperty("superusername"), prop.getProperty("superpassword"))
                .header("Authorization",token)
                .when()
                .contentType(ContentType.JSON)
                .body(payload);
        response = request.post(updatedurl);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());
        return response;
    }

    public Response putOperation(String endpoint,String payload,String token) throws IOException {
        prop=loadConfig();
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        String updatedurl=BaseURL+endpoint;
        System.out.println("--------------updatedurl:"+updatedurl);
        request=given().log().all()
                .auth().basic(prop.getProperty("superusername"), prop.getProperty("superpassword"))
                .header("Authorization",token)
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization",token)
                .body(payload);
        response = request.put(updatedurl);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());
        return response;
    }


    public Response deleteOperation(String endpoint,String token) throws IOException {
        prop=loadConfig();
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        request=given().log().all()
                .auth().basic(prop.getProperty("superusername"), prop.getProperty("superpassword"))
                .header("Authorization",token)
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization",token);
        response = request.delete(BaseURL+endpoint);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());
        return response;
    }

    public Response postOperation_Nonadmin(String endpoint,String payload,String token,String username,String password) throws IOException {
        prop=loadConfig();
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        String updatedurl=BaseURL+endpoint;
        System.out.println("--------------updatedurl:"+updatedurl);
        request=given().log().all()
                .auth().basic(username,password)
                .header("Authorization",token)
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization",prop.getProperty("token"))
                .body(payload);
        response = request.post(updatedurl);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());
        return response;
    }

    public Response postFormOperation(String endpoint,String payload,String token,String key) throws IOException {
        prop=loadConfig();
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        String updatedurl=BaseURL+endpoint;
        System.out.println("--------------updatedurl:"+updatedurl);
        request=given().log().all()
                .contentType("multipart/form-data")
                .multiPart(key,payload)
                .auth().basic(prop.getProperty("superusername"), prop.getProperty("superpassword"))
                .header("Authorization",token)
                .when()
                .header("Authorization",prop.getProperty("token"));
        response = request.post(updatedurl);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());
        return response;
    }

    public Response postMultipleFormOperation(String endpoint,String payload,String payload1,String payload2,String token,String key,String key1,String key2) throws IOException {
        prop=loadConfig();
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        String updatedurl=BaseURL+endpoint;
        System.out.println("--------------updatedurl:"+updatedurl);
        request=given().log().all()
                .contentType("multipart/form-data")
                .multiPart(key,payload)
                .multiPart(key1,payload1)
                .multiPart(key2,payload2)
                .when()
                .header("Authorization",token);
        response = request.post(updatedurl);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());
        return response;
    }


    public Response putFormOperation(String endpoint,String payload,String token,String key) throws IOException {
        prop=loadConfig();
        String BaseURL=prop.getProperty("host");
        System.out.println("--------------baseurl:"+BaseURL);
        String updatedurl=BaseURL+endpoint;
        System.out.println("--------------updatedurl:"+updatedurl);
        request=given().log().all()
                .contentType("multipart/form-data")
                .multiPart(key,payload)
                .auth().basic(prop.getProperty("superusername"), prop.getProperty("superpassword"))
                .header("Authorization",token)
                .when()
                .header("Authorization",prop.getProperty("token"));
        response = request.put(updatedurl);
        System.out.println(response.asString());
        System.out.println(response.getStatusCode());
        return response;
    }

}