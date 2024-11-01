import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ReqResTest {
    private static final String BASE_URI = "https://reqres.in";

    @BeforeClass
    public void setup() {
        baseURI = BASE_URI;
    }

    @Test(priority=1)
    public void testGetUsers(){
         String responseBody = given().log().all()
            .when().
            get("/api/users?page=2")
            .then().statusCode(200)
            .log().all()
            .extract()
            .body()
            .asString();

        Assert.assertTrue(responseBody.contains("data"));
    }

    @Test(priority=2)
    public void testPostUser(){
        String response = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"name\": \"Juan Anzola\",\n" +
                        "    \"job\": \"QA Automation\"\n" +
                        "}")
                .when().
                post("/api/users")
                .then().statusCode(201)
                .log().all()
                .extract()
                .body()
                .asString();

        Assert.assertTrue(response.contains("id"), "La respuesta NO contiene un ID de usuario");
    }

    @Test(priority=3)
    public void testPutUser(){
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"name\": \"Pablo Casallas\", \n" +
                        "  \"job\": \"Developer and MVP\"}")
                .when()
                .put("/api/users/2")
                .then().statusCode(200)
                .log().all()
                .extract()
                .response();

        String name = response.jsonPath().getString("name");
        Assert.assertEquals(name, "Pablo Casallas");
        Assert.assertEquals(response.getStatusCode(), 200, "El codigo del estado NO es 200");
    }

    @Test(priority=4)
    public void testDeleteUser(){
        int userId = 2;
        Response response = given()
                .when()
                .delete("/api/users/" + userId)
                .then()
                .statusCode(204)
                .log().all()
                .extract()
                .response();

        Assert.assertEquals(response.getStatusCode(), 204, "El codigo del estado NO es 204");
    }
}
