package com.common_code;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.number.OrderingComparison.lessThanOrEqualTo;

public class SharedCode {

    public static Logger log = LogManager.getLogger(SharedCode.class);
    private static Response response;
    private static final boolean PRINTERHEADER = true;
    public static ExtentHtmlReporter reporter;
    public static ExtentReports extent;
    public static ExtentTest test;

    @BeforeSuite(alwaysRun = true)
    public void setUp(){
        baseURI = "https://api.github.com";
        setUpReport();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown(){
        //Send final result to the Extent report
        extent.flush();
        reset();
    }

    public static Response getTheResponse(String apiType, RequestSpecification requestSpec){
        //API response time
        long responseTime = 3000L;
        String accessToken = "0c86b0d3a469ea6880d8414826b7828047c203db";
        switch(apiType.toLowerCase()){
            case "post": //<-- Post
                response =
                    given().
                        spec(requestSpec).
                        log().
                        all().
                    when().
                        post().
                    then().
                        time(lessThanOrEqualTo(responseTime)).
                        log().
                        all().
                        extract().
                        response();
                break;
            case "get": //<-- Get
                response =
                    given().
                        spec(requestSpec).
                        contentType(MediaType.APPLICATION_JSON).
                        auth().
                            preemptive().
                            oauth2(accessToken).
                        log().
                        all().
                    when().
                        get().
                    then().
                        time(lessThanOrEqualTo(responseTime)).
                        log().
                        all().
                        extract().
                        response();
                break;
            case "put": //<-- Put
                response =
                    given().
                        spec(requestSpec).
                        log().
                        all().
                    when().
                        put().
                    then().
                        time(lessThanOrEqualTo(responseTime)).
                        log().
                        all().
                        extract().
                        response();
                break;
            case "patch": //<--- Patch
                response =
                    given().
                        spec(requestSpec).
                        log().
                        all().
                    when().
                        patch().
                    then().
                        time(lessThanOrEqualTo(responseTime)).
                        log().
                        all().
                        extract().
                        response();
                break;
            case "delete": //<--- Delete
                response =
                    given().
                        spec(requestSpec).
                        log().
                        all().
                    when().
                        delete().
                    then().
                        time(lessThanOrEqualTo(responseTime)).
                        log().
                        all().
                        extract().
                        response();
                break;
        }
        return response;
    }

    public static String convertCharset(String q) throws UnsupportedEncodingException {

        String result = java.net.URLDecoder.decode(q,"UTF-8");
        return result;
    }

    public static void header(String testName){

        if(PRINTERHEADER){
            log.info
                    ( "\n===================================================================================================="
                    + "\n====> Test Case Name: " +  ""
                    + "\n====> Test Case Number: " +  ""
                    + "\n====> Method Name: " + testName
                    + "\n====================================================================================================");
        }
    }

    public static void setUpReport(){

        //Configure Extent Report
        reporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/src/test/resources/test_automation_api.html");
        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("OS","Windows");
        extent.setSystemInfo("Hostname","Ken");
        extent.setSystemInfo("Environment","QA");
        extent.setSystemInfo("User Name","Ken Caradine");

        reporter.config().setDocumentTitle("Automation Testing in Demo Report");
        reporter.config().setReportName("QA Team Report");
        reporter.config().setChartVisibilityOnOpen(true);
        reporter.config().setTestViewChartLocation(ChartLocation.TOP);
        reporter.config().setTheme(Theme.STANDARD);
    }

    public static void theResult(ITestResult result){
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