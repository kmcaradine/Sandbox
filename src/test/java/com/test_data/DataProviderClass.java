package com.test_data;

import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;

public class DataProviderClass {

    @DataProvider(name="SearchProvider")
    public static Object[][] getDataFromDataProvider(Method method){
        String testCase = method.getName();

        if(testCase.equalsIgnoreCase("SearchRepositoriesTest")) {
            return new Object[][]{
                    {"Valid data with forks", "tetris+language:assembly","forks","desc","application/vnd.github.mercy-preview+json", 200},
                    {"Valid data with stars", "tetris+language:assembly","stars","desc", "application/vnd.github.mercy-preview+json", 200},
                    {"Valid data with update", "tetris+language:assembly", "updated", "asc", "application/vnd.github.mercy-preview+json", 200},
                    {"No request language", "", "updated", "asc",  "application/vnd.github.mercy-preview+json", 422},
                    {"Valid data query", "topic/:ruby+topic:rails", "", "", "application/vnd.github.v3.text-match+json", 200}
            };
        }
        else if(testCase.equalsIgnoreCase("SearchCommitsTest")) {
            return new Object[][]{
                    {"Valid search", "repo:octocat/Spoon-Knife+css", "", "","application/vnd.github.cloak-preview", 200}
            };
        }
        else if(testCase.equalsIgnoreCase("SearchCodeTest")) {
            return new Object[][]{
                    {"Valid search", "addClass+in:file+language:js+repo:jquery/jquery", "", "", "", 200}
            };
        }
        else if(testCase.equalsIgnoreCase("SearchIssuesTest")) {
            return new Object[][]{
                    {"Valid search", "windows+label:bug+language:python+state/:open", "created", "asc", "application/vnd.github.symmetra-preview+json", 200}
            };
        }
        else if(testCase.equalsIgnoreCase("SearchUsersTest")) {
            return new Object[][]{
                    //{"Valid search", "tom repos:>42 followers:>1000", "", "", "application/vnd.github.v3.text-match+json", 200}
                    {"Valid search", "tom+repos:%3E42+followers:%3E1000", "", "", "application/vnd.github.v3.text-match+json", 200}
            };
        }
        else if(testCase.equalsIgnoreCase("SearchTopicsTest")) {
            return new Object[][]{
                    {"Valid search", "ruby+is:featured", "", "", "application/vnd.github.mercy-preview+json", 200}
            };
        }
        else if(testCase.equalsIgnoreCase("SearchLabelsTest")) {
            return new Object[][]{
                    {"Valid search", 64778136, "bug+defect+enhancement","", "", "application/vnd.github.symmetra-preview+json", 200}
            };
        }
        else if(testCase.equalsIgnoreCase("TextMatchMetadataTest")) {
            return new Object[][]{
                    {"Valid search", "windows+label:bug+language:python+state:open","created", "asc", "application/vnd.github.v3.text-match+json", 200}
            };
        }
        else{
            return null;
        }
    }
}