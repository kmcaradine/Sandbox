package TestData;

import org.testng.annotations.DataProvider;
import java.lang.reflect.Method;

public class DataProviderClass {

    @DataProvider(name="SearchProvider")
    public static Object[][] getDataFromDataprovider(Method method){
        String testCase = method.getName();

        if(testCase.equalsIgnoreCase("SearchRepositoriesTest")) {
            return new Object[][]{
                    {testCase, "tetris+language:assembly","forks","desc","application/vnd.github.v3.text-match+json", 200},
                    {testCase, "tetris+language:assembly","stars","desc", "application/vnd.github.v3.text-match+json", 200},
                    {testCase, "tetris+language:assembly", "updated", "asc", "application/vnd.github.v3.text-match+json", 200},
                    {"No request language", "", "updated", "asc",  "application/vnd.github.v3.text-match+json", 422}
            };
        }
        else{
            return null;
        }
    }
}