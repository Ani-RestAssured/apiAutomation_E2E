
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
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
//When we use String Extraction
                String responseString = reqLogin.when().post("api/ecom/auth/login")
                                        .then().extract().response().asString();

//When we use deserialization
          LoginResponse loginResponse = reqLogin.when().post("api/ecom/auth/login")
                                        .then().extract().response().as(LoginResponse.class);

        System.out.println(loginResponse.getToken());
        System.out.println(loginResponse.getUserId());

    }
}
