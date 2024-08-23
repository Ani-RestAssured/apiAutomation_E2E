
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.Filter;
import io.restassured.http.*;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.*;
import pojo.LoginRequest;
import pojo.LoginResponse;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class EcommerceAPITest {

    public static void main (String[] args)
    {
        RequestSpecification req =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                                         .setContentType(ContentType.JSON)
                                         .build();

        LoginRequest lR = new LoginRequest();

        lR.setUserEmail("Learner@gmail.com");
        lR.setUserPassword("Learner@1234");

        RequestSpecification reqLogin = given().spec(req).body(lR);


//When we use deserialization
        LoginResponse loginResponse = reqLogin.when().post("api/ecom/auth/login")
                                      .then().extract().response().as(LoginResponse.class);

        System.out.println(loginResponse.getToken());
        String authToken = loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        String userID = loginResponse.getUserId();

        RequestSpecification addProductBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                                                                         .addHeader("Authorization",authToken)
                                                                         .build();

        RequestSpecification addProduct = given().log().all().spec(addProductBaseReq).param("productName","Peter England")
                                                   .param("productAddedBy",userID)
                                                   .param("productCategory","fashion")
                                                   .param("productSubCategory","shirts")
                                                   .param("productPrice",1240)
                                                   .param("productDescription","Addias Originals")
                                                   .param("productFor","Men")
                                                   .multiPart("productImage",new File ("C:\\Users\\DELL\\Downloads\\image016.jpg"));

//When we use String Extraction
        String addProductResponseString = addProduct.when().post("/api/ecom/product/add-product")
                                          .then().log().all().extract().response().asString();

        JsonPath  js = new JsonPath(addProductResponseString);

        String productId = js.get("productId");
    }
}
