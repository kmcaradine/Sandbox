package com.NetworkAPIs;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

public class SharedCode {

    static Logger log = LogManager.getLogger(SharedCode.class);
    private static Response response;
    private static final boolean PRINTERHEADER = true;

    public static ExtentHtmlReporter reporter;
    public static ExtentReports extent;
    public static ExtentTest test;

    public static Response getTheResponse(String apiType, RequestSpecification requestSpec){

        //API response time
        long responseTime = 3000L;
        switch(apiType.toLowerCase()){
            case "post": //<-- Post
                response =
                    given().
                        spec(requestSpec).
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON).
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
                        accept(MediaType.APPLICATION_JSON).
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
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON).
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
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON).
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
                        contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON).
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
}