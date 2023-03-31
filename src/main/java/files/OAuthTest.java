package files;

import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;
public class OAuthTest {
    public static void main(String[] args){

//        String email="mishakaspar@gmail.com";
//        String pass="QWEasdzxcMike!@#1";
//       System.setProperty("webdriver.chrome.driver","D://Education//RAFramework//WD//chromedriver_win32//chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
//       // WebDriverManager.chromedriver().setup();
//        Thread.sleep(3000);
//        driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifystring");
//        Thread.sleep(3000);
//        driver.findElement(By.xpath("//input[@type='email']")).sendKeys(email);
//        driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.ENTER);
//        driver.findElement(By.xpath("(//input[@type='text'])[1]")).sendKeys(pass);
//        driver.findElement(By.xpath("(//input[@type='text'])[1]")).sendKeys(Keys.ENTER);
//        Thread.sleep(4000);
//        String currentUrl1 = driver.getCurrentUrl();

        String currentUrl = "https://rahulshettyacademy.com/getCourse.php?state=verifystring&code=4%2F0AVHEtk5d8QJ1QravI_AJc7Iha6fQuCPUSa1CPdCXCuVux3pQxWSTjH4I9vcQcHcYDMF1fw&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=consent";

        String partialCode = currentUrl.split("code=")[1];
        String code = partialCode.split("&scope")[0];
        System.out.println("code = " + code);

        String accessTokenResponse = given().urlEncodingEnabled(false)
                .queryParams("code", code)
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code")
                .when()
                .post("https://www.googleapis.com/oauth2/v4/token")
                .asString();
        JsonPath js = new JsonPath(accessTokenResponse);
        String access_token = js.getString("access_token");


        String response = given()
                .queryParam("access_token", "access_token")
                .when()
                .get("https://rahulshettyacademy.com/getCourse.php")
                .asString();
        System.out.println("response = " + response);
    }
}
