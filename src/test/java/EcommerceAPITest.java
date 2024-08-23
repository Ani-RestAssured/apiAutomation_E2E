
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
import pojo.PlaceOrderRequest;
import pojo.PlaceOrderRequest_OrderDetails;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class EcommerceAPITest {

    public static void main (String[] args)
    {
//Create New User
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

//Add Product
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

//Place Order

//Serializing the parameters

        PlaceOrderRequest_OrderDetails por_od = new PlaceOrderRequest_OrderDetails();
        por_od.setCountry("India");
        por_od.setProductOrderedId(productId);

        List<PlaceOrderRequest_OrderDetails> orderDetails = new ArrayList<PlaceOrderRequest_OrderDetails>();
        orderDetails.add(por_od);

        PlaceOrderRequest por = new PlaceOrderRequest();
        por.setOrder(orderDetails);

        RequestSpecification createOrderBaseReq = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                                    .setContentType(ContentType.JSON)
                                    .addHeader("Authorization",authToken)
                                    .build();

        RequestSpecification createOrder = given().log().all().spec(createOrderBaseReq).body(por);

        String responseCreateOrder = createOrder.when().post("/api/ecom/order/create-order").then().log().all().extract().response().asString();

        JsonPath js2 = new JsonPath(responseCreateOrder);

        System.out.println(js2);

    }
}
