package files;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraTests {
    public static void main(String[] args) {
        baseURI = "http://localhost:8080";
        //using before .when()
        SessionFilter sessionFilter = new SessionFilter();
        //login scenario
        String responseWithToken = given().contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"username\": \"Mykhailo\",\n" +
                        "    \"password\": \"erasdzxcX21@!1\"\n" +
                        "}")
                .log().all()
                //put session filter
                .filter(sessionFilter)
                .when()
                .post("/rest/auth/1/session")
                .then()
                .log().all()
                .extract().response().toString();

        String expectedMessage = "Hi how are you?";
        //add comment
        String addCommentResponse = given().pathParam("issueIdOrKey", "10007").log().all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"body\": \"" + expectedMessage + "\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}")
                //we also need to add sessionFilter before .when()
                .filter(sessionFilter)
                .when()
                .post("/rest/api/2/issue/{issueIdOrKey}/comment")
                .then()
                .assertThat()
                .statusCode(201)
                .extract().response().asString();
        JsonPath js = new JsonPath(addCommentResponse);
        String commentId = js.getString("id");

        //add attachment
//        given().header("X-Atlassian-Token","no-check")
//                .filter(sessionFilter)
//                .pathParam("issueIdOrKey", "10007")
//                .header("Content-Type", "multipart/form-data")
//                .multiPart("file", new File("jira.txt"))
//                .when()
//                .log().all()
//                .post("/rest/api/2/issue/{issueIdOrKey}/attachments")
//                .then()
//                .log().all()
//                .assertThat()
//                .statusCode(200);


        // get issue
        String issueDetails = given()
                .filter(sessionFilter)
                .pathParam("issueIdOrKey", "10007")
                .queryParam("fields", "comment")
                .when()
                .get("/rest/api/2/issue/{issueIdOrKey}")
                .then()
                .log().all()
                .extract()
                .response()
                .asString();
        System.out.println("issueDetails = " + issueDetails);
        String actualMessage="";
        JsonPath js1 = new JsonPath(issueDetails);
        int commentCount = js1.getInt("fields.comment.comments.size()");
        System.out.println("commentCount = " + commentCount);
        for (int i = 0; i < commentCount; i++) {
            String commentIdIssue = js1.get("fields.comment.comments[" + i + "].id").toString();
            if (commentIdIssue.equalsIgnoreCase(commentId)) {
                actualMessage = js1.get("fields.comment.comments[" + i + "].body").toString();
                System.out.println("actualMessage = " + actualMessage);
            }
        }
        Assert.assertEquals(actualMessage,expectedMessage);

    }


}
