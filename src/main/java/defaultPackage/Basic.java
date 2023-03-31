package defaultPackage;

import files.Payload;
import files.ReusableMethods;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basic {
    public static void main(String[] args) {

        //validate add place
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String responce = given().log().all()
                .queryParam("key", "qaclick123")
                .contentType(ContentType.JSON)
                .body(Payload.addPlace())
                .when()
                .post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("server", "Apache/2.4.41 (Ubuntu)")
                .extract().response().asString();

        System.out.println("responce = " + responce);
        JsonPath js = new JsonPath(responce); //for parsing JSON
        String place_id = js.getString("place_id");
        System.out.println("place_id = " + place_id);

        String newAddress = "Summer walk, Africa";
        given().log().all()
                .queryParam("key", "qaclick123")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "\"place_id\":\"" + place_id + "\",\n" +
                        "\"address\":\"" + newAddress + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then()
                .assertThat().log().all().statusCode(200)
                .body("msg", equalTo("Address successfully updated"));


        //get place
        String getPlaceResponce = given().log().all()
                .queryParam("key", "qaclick123")
                .queryParam("place_id", place_id)
                .when()
                .get("/maps/api/place/get/json")
                .then()
                .assertThat().log().all()
                .statusCode(200)
                .extract().response().asString();

        JsonPath js1 =  ReusableMethods.rawToJson(getPlaceResponce);
        String actualAddress = js1.getString("address");
        System.out.println("actualAddress = " + actualAddress);
        //junit, testNG

        Assert.assertEquals(actualAddress, newAddress);

    }
}
