package com.NetworkAPIs;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class SearchRepositories extends SharedCode {

    private RequestSpecBuilder requestSpecBuilder ;
    private RequestSpecification requestSpec;
    private Response response;
    private String apiType = "";
    private String testName = "";

    @BeforeClass(alwaysRun = true)
    public void setUp(){
        setUpReport();
    }

    @Test
    public void SearchRepositoriesTest(Method method){

        apiType = "get";
        testName = method.getName();

        //Print header to console
        header(testName);

        test = extent.createTest(testName,"SearchRepositories");

        //Send the Base URL and Base Path  the report
        String baseURI = "https://api.github.com";
        String basePath = "/search/repositories";
        test.log(Status.INFO, "Base URI: " + baseURI);
        test.log(Status.INFO, "Base Path: " + basePath);

        //Build API request
        requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(baseURI);
        requestSpecBuilder.setBasePath(basePath);
        requestSpecBuilder.addQueryParam("q","tetris+language:assembly");
        requestSpecBuilder.addQueryParam("sort","stars");
        requestSpecBuilder.addQueryParam("order","desc");
        requestSpec = requestSpecBuilder.build();

        //Get API response
        response = getTheResponse(apiType, requestSpec);

        //Send the response to the report
        test.log(Status.INFO, "Response Body: " + response.body().asString());

        //Assert on api response
        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        log.info("Response Status: " + response.statusCode());
    }

    @AfterMethod(alwaysRun = true)
    public void getResult(ITestResult result) {

        //Check for pass, fail or skip test status after each test
        if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, MarkupHelper.createLabel(result.getName() + "Test Case PASSED", ExtentColor.GREEN));
        }
        else if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + "Test Case FAIL", ExtentColor.RED));
            test.fail(result.getThrowable());
        }
        else if (result.getStatus() == ITestResult.SKIP) {
            test.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + "Test Case SKIP", ExtentColor.YELLOW));
            test.skip(result.getThrowable());
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown(){

        //Send result to the Extent report
        extent.flush();
        RestAssured.reset();
    }
}
