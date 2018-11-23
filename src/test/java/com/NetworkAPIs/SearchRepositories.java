package com.NetworkAPIs;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

public class SearchRepositories extends SharedCode {

    private RequestSpecBuilder requestSpecBuilder ;
    private RequestSpecification requestSpec;
    private Response response;
    private String apiType = "";
    private String testName = "";

    @Test
    public void SearchRepositoriesTest(Method method){
        apiType = "get";
        testName = method.getName();

        //Print header to console
        header(testName);

        test = extent.createTest(testName,"SearchRepositories");

        //Set the Base URL and Base Path send it to the report
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

        //Get response time and send the it to the report
        long timeSeconds = response.timeIn(SECONDS);
        test.log(Status.INFO, "Response Time: " + timeSeconds + " second(s)");

        //Send the response body to the report
        test.log(Status.INFO, "Response Body: " + response.body().asString());

        //Assert on api response
        assertThat(response.statusCode(), is(HttpStatus.SC_OK));
        //assertThat(response.contentType(), is(ContentType.JSON));
        assertThat(timeSeconds, is(lessThanOrEqualTo(4L)));
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
}
