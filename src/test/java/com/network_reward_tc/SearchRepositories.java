package com.network_reward_tc;

import com.test_data.DataProviderClass;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.common_code.SharedCode;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class SearchRepositories extends SharedCode {

    private RequestSpecBuilder requestSpecBuilder ;
    private RequestSpecification requestSpec;
    private Response response;
    private String apiType = "";
    private String testName = "";

    @Test (dataProvider="SearchProvider",
           dataProviderClass=DataProviderClass.class)
    public void SearchRepositoriesTest(String tcName, String lang, String sort, String order,
                                       String acceptHeader, int requestStatus){
        apiType = "get";
        testName = tcName;

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
        if(acceptHeader.trim().length() > 0){
            requestSpecBuilder.setAccept(acceptHeader);
        }
        if(lang.trim().length() > 0) {
            requestSpecBuilder.addQueryParam("q", lang);
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
