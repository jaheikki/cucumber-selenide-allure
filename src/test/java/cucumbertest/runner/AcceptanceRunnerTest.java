package cucumbertest.runner;

import com.codeborne.selenide.Configuration;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import microservice.common.MsConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import static microservice.helper.SeleniumHelper.*;

@RunWith(Cucumber.class)
@CucumberOptions(
        //format={"json:target/Destination/cucumber.json", "pretty", "html:target/cucumber"},
        //format = { "pretty", "html:target/cucumber" },
        //features = { "classpath:catalog/tests" },
        features = { "classpath:acceptance/tests" },
        glue = {"microservice/testlibraries"}
)

public class AcceptanceRunnerTest {

    @BeforeClass
    public static void setUp(){
        printMethodName();

        // default folder not good
        Configuration.reportsFolder = MsConstants.selenideScreenshotsFolder;

        //default timeout (env might be very slow)
        Configuration.timeout = MsConstants.commonSelenideTimeout;

        //If true, Selenide uses Javascript to click element -> can be used as temporary workaround if bugs related clicking (especially in Chrome)
        Configuration.clickViaJs = MsConstants.selenideClickElementByJavascript;


    }

    @AfterClass
    public static void tearDown(){
        printMethodName();

    }
}
