package cucumbertest.runner;

import com.codeborne.selenide.Configuration;
//import cucumber.api.CucumberOptions;
//import cucumber.api.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import microservice.common.MsVariables;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import static microservice.helper.SeleniumHelper.printMethodName;

@CucumberOptions(
        features = {"src/test/resources/acceptancetests/"},
        glue = {"teststepdefinitions"},
        //tags = "@simple_test"
        //plugin = {"io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"}
        //
        plugin = {"io.qameta.allure.cucumber5jvm.AllureCucumber5Jvm"}
//        tags = {"@simple_test"

        )

public class AcceptanceRunnerTest extends AbstractTestNGCucumberTests {

    @BeforeClass
    public static void setUp(){
        printMethodName();

        // default folder not good
        Configuration.reportsFolder = MsVariables.selenideScreenshotsFolder;

        //default timeout (env might be very slow)
        Configuration.timeout = MsVariables.commonSelenideTimeout;

        //If true, Selenide uses Javascript to click element -> can be used as temporary workaround if bugs related clicking (especially in Chrome)
        Configuration.clickViaJs = MsVariables.selenideClickElementByJavascript;

    }

    @AfterClass
    public static void tearDown(){
        printMethodName();

    }
}

