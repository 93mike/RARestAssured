package defaultPackage;

import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJSONParce {
    public static void main(String[] args) {
        JsonPath js = new JsonPath(Payload.CoursePrice());
        int count = js.getInt("courses.size()");
        System.out.println("count = " + count);
        int totalAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println("totalAmount = " + totalAmount);
        //print title of first course
        String firstCourseTitle = js.get("courses[0].title");
        System.out.println("firstCourseTitle = " + firstCourseTitle);

        //print title of last course
        String lastCourseTitle = js.get("courses[" + (count - 1) + "].title");
        System.out.println("lastCourseTitle = " + lastCourseTitle);
        System.out.println();
        System.out.println();

        //all title and prices
        for (int i = 0; i < count; i++) {
            String courseTitles = js.get("courses[" + i + "].title");
            System.out.print("courseTitles = " + courseTitles + " : ");
            int coursePrice = js.get("courses[" + i + "].price");
            System.out.println("coursePrice = " + coursePrice);
        }

        //value of copies when title is RPA
        System.out.println("value of copies when title is RPA");
        for (int i = 0; i < count; i++) {
            String courseTitles = js.get("courses[" + i + "].title");
            if (courseTitles.equalsIgnoreCase("RPA")){
                //copies sold
                int copies = js.get("courses[" + i + "].copies");
                System.out.println("copies = " + copies);
                break;
            }
        }
    }
}
