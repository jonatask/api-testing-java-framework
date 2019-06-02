package health_checks;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import employee.mother.EmployeeMother;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import utils.Utils;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;

public class HealthChecks {

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
     * Verify that the endpoint of POST employee is active.
     ******************************************************/
    @Test
    public void postEmployee_healthCheck() {

        EmployeeMother.createValidEmployee();

            given().
                    spec(requestSpec).
                    and().
                        body(EmployeeMother.getValidEmployee()).
                    when().
                        post("/v1/create").
                    then().
                        assertThat().
                        statusCode(200);
    }
}