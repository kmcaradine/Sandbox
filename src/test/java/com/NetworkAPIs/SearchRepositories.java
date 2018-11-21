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
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SearchRepositories extends SharedCode {

    private RequestSpecBuilder builder;
    private RequestSpecification requestSpec;
    private Response response;
    private String apiType = "";
    private String testName = "";

    @BeforeSuite(alwaysRun = true)
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

        String baseURI = "https://api.github.com/search/repositories";
        //Send the URL the report
        test.log(Status.INFO, "Base URI: " + baseURI);

        //Build API request
        builder = new RequestSpecBuilder();
        builder.setBaseUri(baseURI);
        builder.addQueryParam("q","tetris+language:assembly");
        builder.addQueryParam("sort","stars");
        builder.addQueryParam("order","desc");
        requestSpec = builder.build();

        //Get API response
        response = getTheResponse(apiType, requestSpec);

        //Send the response to the report
        test.log(Status.INFO, "Response Body: " + response.body().prettyPrint());

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

    @AfterSuite(alwaysRun = true)
    public void tearDown(){

        //Send result to the Extent report
        extent.flush();
        RestAssured.reset();
    }
}
