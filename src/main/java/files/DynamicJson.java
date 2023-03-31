package files;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {
    @Test
    public void addBook(){
        Faker faker= new Faker();
        String fakeName= faker.name().name();
        String fakeBookId = faker.number().digit().toString();
        System.out.println("fakeName = " + fakeName);
        System.out.println("fakeBookId = " + fakeBookId);
        RestAssured.baseURI="http://216.10.245.166";
        String response = given()
                .contentType(ContentType.JSON)
                .body(Payload.addBook(fakeName,fakeBookId))
                .when()
                .post("/Library/Addbook.php")
                .then()
                .assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = ReusableMethods.rawToJson(response);
        String id = js.get("ID");
        System.out.println("id = " + id);
    }

    @DataProvider(name= "BooksData")
    public Object[][] getData(){
        //array = collection of elements
         return new Object[][]{{"safd","1244"} ,{"safdss","12441"},{"safdd","12434"}};
    }

    @Test(dataProvider = "BooksData" )
    public void addBookByDataProvider(String isbn, String aisle){
        RestAssured.baseURI="http://216.10.245.166";
        String response = given()
                .contentType(ContentType.JSON)
                .body(Payload.addBook(isbn,aisle))
                .when()
                .post("/Library/Addbook.php")
                .then()
                .assertThat().statusCode(200)
                .extract().response().asString();
        JsonPath js = ReusableMethods.rawToJson(response);
        String id = js.get("ID");
        System.out.println("id = " + id);
    }
}
