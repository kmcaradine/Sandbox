package com.network_reward_tc;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.common_code.SharedCode;
import com.test_data.DataProviderClass;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class SearchCode extends SharedCode {

    private RequestSpecBuilder requestSpecBuilder ;
    private RequestSpecification requestSpec;
    private Response response;
    private String apiType = "";
    private String testName = "";

    @Test(dataProvider="SearchProvider",
            dataProviderClass= DataProviderClass.class)
    public void SearchCodeTest(String tcName, String lang, String sort, String order,
                               String acceptHeader, int requestStatus) throws UnsupportedEncodingException {
        apiType = "get";
        testName = tcName;

        //Print header to console
        header(testName);

        //String accessToken = "0c86b0d3a469ea6880d8414826b7828047c203db";
        //RequestSpecification oauth2(accessToken);

        test = extent.createTest(testName,"SearchCommits");

        //Set the Base URL and Base Path send it to the report
        String basePath = "/search/code";
        test.log(Status.INFO, "Base URI: " +  RestAssured.baseURI);
        test.log(Status.INFO, "Base Path: " + basePath);

        //Build API request
        requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBasePath(basePath);
        if(acceptHeader.trim().length() > 0){
            requestSpecBuilder.setAccept(acceptHeader);
        }
        if(lang.trim().length() > 0) {
            requestSpecBuilder.addQueryParam("q", convertCharset(lang));
        }
        if(sort.trim().length() > 0) {
            requestSpecBuilder.addQueryParam("sort", sort);
        }
        if(order.trim().length() > 0) {
            requestSpecBuilder.addQueryParam("order", order);
        }

        requestSpec = requestSpecBuilder.build();

        //Get API response
        response = getTheResponse(apiType, requestSpec);

        //Get response time and send the it to the report
        long timeSeconds = response.timeIn(MILLISECONDS);
        test.log(Status.INFO, "Response Time: " + timeSeconds + " millisecond(s)");

        //Send the response status code to the report
        test.log(Status.INFO, "Response Status: " + response.statusCode() );
        log.info("Response Status: " + response.statusCode());

        //Send the response body to the report
        //test.log(Status.INFO, "Response Body: " + response.body().asString());

        //Assert on api response
        assertThat(response.statusCode(), is(requestStatus));
        assertThat(timeSeconds, is(lessThanOrEqualTo(3000L)));
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
