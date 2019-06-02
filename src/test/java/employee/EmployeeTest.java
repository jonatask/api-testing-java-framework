package employee;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.*;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import employee.mother.EmployeeMother;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.*;
import org.junit.*;
import utils.Utils;

import java.io.File;

public class EmployeeTest {

    private static RequestSpecification requestSpec;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(options());

    @BeforeClass
    public static void createRequestSpecification() {

        requestSpec = new RequestSpecBuilder().
                setBaseUri(Utils.BASE_URI).
                setContentType(ContentType.JSON).
                build();
    }

    /*******************************************************
     * Verify that the response of POST employee matches the json schema.
     ******************************************************/
    @Test
    public void postEmployee_checkSchema() {

        EmployeeMother.createValidEmployee();

        given().
                spec(requestSpec).
                and().
                body(EmployeeMother.getValidEmployee()).
                when().
                post("/v1/create").
                then().
                assertThat().
                body(matchesJsonSchema(new File("src/test/java/employee/schema/post-employee.json")));
    }

    /*******************************************************
     * Create an employee in the API, validate if this employee
     * was created correctly and then delete the employee.
     ******************************************************/
    @Test
    public void postEmployee_verifySavedEmployee() {

        EmployeeMother.createValidEmployee();

        JsonPath jsonPostResponse =
                given().
                        spec(requestSpec).
                        and().
                            body(EmployeeMother.getValidEmployee()).
                        when().
                            post("/v1/create").
                        then().
                            assertThat().
                            statusCode(200).
                            extract().
                            body().jsonPath();

        String id = jsonPostResponse.get("id");

        Assert.assertThat(jsonPostResponse.get("name"), is(EmployeeMother.getValidEmployee().getName()));
        Assert.assertThat(jsonPostResponse.get("salary"), is(EmployeeMother.getValidEmployee().getSalary()));
        Assert.assertThat(jsonPostResponse.get("age"), is(EmployeeMother.getValidEmployee().getAge()));

        JsonPath jsonGetResponse =
                given().
                        spec(requestSpec).
                        when().
                            get("/v1/employee/" + id).
                        then().
                            assertThat().
                            statusCode(200).
                            extract().
                            body().jsonPath();

        Assert.assertThat(jsonGetResponse.get("id"), is(id));
        Assert.assertThat(jsonGetResponse.get("employee_name"), is(EmployeeMother.getValidEmployee().getName()));
        Assert.assertThat(jsonGetResponse.get("employee_salary"), is(EmployeeMother.getValidEmployee().getSalary()));
        Assert.assertThat(jsonGetResponse.get("employee_age"), is(EmployeeMother.getValidEmployee().getAge()));

        given().
                spec(requestSpec).
                when().
                    delete("/v1/delete/" + id).
                then().
                    assertThat().
                    statusCode(200);
    }
}