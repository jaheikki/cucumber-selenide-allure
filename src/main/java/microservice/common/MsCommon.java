package microservice.common;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.Scenario;
import microservice.demo.SSHExamples;
import microservice.helper.SeleniumHelper;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static microservice.common.MsVariables.*;
import static microservice.helper.SeleniumHelper.printMethodName;

public class MsCommon {

    private static final Logger log = LoggerFactory.getLogger(MsCommon.class);

    public static void setUp() throws Exception {

        // default folder not good
        //Configuration.reportsFolder = OnlinefirstConstants.onlinefirstSelenideReportsFolder;

        // default timeout (env might be very slow)
        //Configuration.timeout = MsVariables.commonSelenideTimeout;

        //If true, Selenide uses Javascript to click element -> can be used as temporary workaround if bugs related clicking (especially in Chrome)
        //Configuration.clickViaJs = selenideClickElementByJavascript;

        //Prevent selenide closing browser, because we want to logout OE before closing browser
        //See: logoutAndCloseBrowser()
        //Configuration.holdBrowserOpen=true;

        //Configuration.browser="firefox";
        Configuration.browser="chrome";

    }

    public static void waitForElementClick(String clickLocator, String verifyLocator)  {
        log.info(printMethodName());

        try {
            waitForElementClick(elementClickTimeoutMs, elementClickRetryIntervalMs, elementClickXpathWaitTimeoutMs, elementClickAngularWaitTimeoutMs, -150, false, clickLocator, verifyLocator, doRefreshOnFailure);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void waitForElementClick(String clickLocator, String verifyLocator, boolean isAngular)  {
        log.info(printMethodName());

        try {
            waitForElementClick(elementClickTimeoutMs, elementClickRetryIntervalMs, elementClickXpathWaitTimeoutMs, elementClickAngularWaitTimeoutMs, -150, isAngular, clickLocator, verifyLocator,doRefreshOnFailure);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /*
        waitForElementClick:
        - Retries clicking element in loop until timeout occurs
        - Angular click&wait supported, see isAngular option
        - Catches any exceptions -> loop if exception exists
        - Scrolls near element (by default above element) for chrome to click element (chrome will not click element if it's under other element e.g. navigation pane)
        - Does not click element if we are already in verifyLocator page
        - Both xpath and cssSelector are both supported as locator
        - You can leave the verifyLocator empty, then clickLocator negation ("clickLocator should not be visible anymore") is used instead

        Note: Changes selenide "master" timeout (Configuration.timeout) for enabling quick looping, but the original parameterized timeout will be returned in finally clause.

        */
    public static void waitForElementClick(int timeoutMs, int retryIntervalMs, int locatorWaitTimeoutMs, int angularWaitTimeoutMs, int yAxisPixelsMove, boolean isAngular, String clickLocator, String verifyLocator, boolean doRefreshOnFailure)  {
        log.info(printMethodName());

        boolean verifyLocatorIsEmpty=false;
        if (verifyLocator.equals("")) {
            verifyLocator=clickLocator;
            verifyLocatorIsEmpty = true;
        }

        DateTime timeoutTime = new DateTime().plusMillis(timeoutMs);
        while (timeoutTime.isAfterNow()) {
            try {

                final By seleniumBy = verifyLocator.startsWith("//") ? By.xpath(verifyLocator) : By.cssSelector(verifyLocator);
                if (!$(seleniumBy).isDisplayed() || verifyLocatorIsEmpty) { //verifyLocatorIsEmpty overrides the check if already in target page
                    Configuration.timeout = locatorWaitTimeoutMs; //Override the default selenide timeout - for while retry

                    if (isAngular) waitforAngularJS(angularWaitTimeoutMs);
                    scrollNearElement(0,yAxisPixelsMove, clickLocator);
                    if (isAngular) waitforAngularJS(angularWaitTimeoutMs);
                    clickElement(clickLocator);
                    if (isAngular) waitforAngularJS(angularWaitTimeoutMs);

                    if (verifyLocatorIsEmpty) { //clickLocator should not be visible anymore if verifyLocatorIsEmpty = true
                        verifyElementNotVisible(clickLocator);
                    } else {
                        log.info("Current selenide timeout ms: " +Configuration.timeout);
                        verifyElement(verifyLocator);
                    }
                } else {
                    log.info("Already in correct page: "+verifyLocator);
                }
                return;
            } catch (Throwable e) {
                log.info(new DateTime().toString());
                log.info("Got exception: " + e);
                log.info("Sleeping " + (retryIntervalMs) + " milliseconds");
                sleep(retryIntervalMs);
                if (doRefreshOnFailure) Selenide.refresh();
            } finally {
                log.info(new DateTime().toString());
                Configuration.timeout = MsVariables.commonSelenideTimeout; //force return original configuration timeout
            }
        }
        throw new RuntimeException("Element click failed after " + timeoutMs + " milliseconds.");
    }

    /* Normally scrolled only with y-axis direction, e.g. set yAxisPixelsMove = -150 means scrolling little above the element */
    public static void scrollNearElement(int xAxisPixelsMove,int yAxisPixelsMove, String clickLocator)  {
        log.info(printMethodName());

        if (xAxisPixelsMove != 0 || yAxisPixelsMove != 0)   {
            log.info("Now scrolling near element");
            SeleniumHelper.myFindElementAndScrollWindow(clickLocator, xAxisPixelsMove, yAxisPixelsMove);
        }
    }

    /*Can be used with xpath and cssSelector*/
    public static void clickElement(String clickLocator) {
        log.info(printMethodName());

        log.info("About to click element: " + clickLocator);

        if(clickLocator.startsWith("//")) {
            $(By.xpath(clickLocator)).shouldBe(visible).shouldBe(enabled).click();

        } else {
            $(By.cssSelector(clickLocator)).shouldBe(visible).shouldBe(enabled).click();
        }
        log.info("Click done.");
    }

    /*Can be used with xpath and cssSelector*/
    public static void verifyElement(String verifyLocator) {
        log.info(printMethodName());

        log.info("Waiting for verifyLocator: " + verifyLocator);

        if(verifyLocator.contains("//")) {
            $(By.xpath(verifyLocator)).shouldBe(visible);

        } else {
            $(By.cssSelector(verifyLocator)).shouldBe(visible);
        }

        log.info("Waiting for verifyLocator done.");
    }

    /*Can be used with xpath and cssSelector*/
    public static void verifyElementNotVisible(String verifyLocator) {
        log.info(printMethodName());

        log.info("Waiting for verifyLocator not visible: " + verifyLocator);

        if(verifyLocator.contains("//")) {
            $(By.xpath(verifyLocator)).shouldNotBe(visible);

        } else {
            $(By.cssSelector(verifyLocator)).shouldNotBe(visible);
        }

        log.info("Waiting for verifyLocator done.");
    }


    public static void waitforAngularJS() {

        waitforAngularJS(elementClickAngularWaitTimeoutMs);
    }

    public static void waitforAngularJS(int timeoutInMs) {

        int timeout = timeoutInMs/1000; //convert to seconds

        DateTime date;

        log.info("*** waitforAngularJS() start ***");
        log.info(new DateTime().toString());

        WebDriver driver = getWebDriver();
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        final String query =
                "waitForAngular =function() {" + "  window.angularFinished = false;"
                        + "  var el = document.querySelector('body');"
                        + "  var callback = (function(){window.angularFinished=1});"
                        + "  angular.element(el).injector().get('$browser')."
                        + "      notifyWhenNoOutstandingRequests(callback);};";

        //jsExecutor.executeJavascript(query);
        jsExecutor.executeScript(query);

        try {
            jsExecutor.executeScript("waitForAngular();");
            new WebDriverWait(driver, timeout) {
            }.until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driver) {
                    Object noAjaxRequests = jsExecutor.executeScript("return window.angularFinished;");
                    log.info("noAjaxRequests: "+noAjaxRequests.toString());
                    return "1".equals(noAjaxRequests.toString());

                }
            });
        } catch (Throwable e) {
            String ErrorMsg =
                    "Failed wait for no angular JS request within timeout of : " + timeout + " seconds  at url:"
                            + driver.getCurrentUrl();
            log.info(ErrorMsg);
        }
        log.info("*** waitforAngularJS() end ***");
        log.info(new DateTime().toString());


    }

    public static void navigateBackToProductsPage() {
        printMethodName();

        $(By.cssSelector("#tab-products")).shouldBe(visible).shouldBe(enabled).click();
        $(By.xpath("//span[contains(text(),'Products')]")).shouldBe(visible).shouldBe(enabled).click();

        //FOR TESTING FAILED UI CASE IN ALLURE REPORT
        //$(By.xpath("//span[contains(text(),'Failure')]")).shouldBe(visible).shouldBe(enabled).click();

    }

    /*User of this method needs to 'grep' success of executed command from returned map entries stdout and stderr*/
    public static HashMap<String, ArrayList<String>> executeSystemCommandAndReturnStdOutAndStdErr(String command, boolean debug) throws IOException {
        printMethodName();

        log.info("Executing command: "+command);
        ArrayList<String> stdOut = new ArrayList<String>();
        ArrayList<String> stdErr = new ArrayList<String>();

        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(command);

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // read the output from the command
        if (debug) log.info("Here is the standard output of the command:\n");
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            if (debug) log.info(s);
            stdOut.add(s);

        }

        // read any errors from the attempted command
        if (debug) log.info("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            if (debug) log.info(s);
            stdErr.add(s);
        }

        HashMap<String, ArrayList<String>> stdOutErrMap = new HashMap<>();
        stdOutErrMap.put("stdout",stdOut);
        stdOutErrMap.put("stderr",stdErr);

        return stdOutErrMap;

    }


    public static String printScenarioPhaseText(Scenario scenario, String phase)  {
        List<String> testCaseStartedList = new ArrayList<>();

        testCaseStartedList.add("*********************************************************************");
        testCaseStartedList.add("*********************************************************************");
        testCaseStartedList.add(phase + " OF TEST CASE (SCENARIO): "+scenario.getName());
        testCaseStartedList.add("*********************************************************************");
        testCaseStartedList.add("*********************************************************************");

        String testCaseStartedText = String.join("\n", testCaseStartedList);

        return testCaseStartedText;
    }


    public static void waitUntilMethodSucceeds(Runnable runnable, int timeoutMs, int retryIntervalMs)
    {
        printMethodName();

        log.info("Method name: "+runnable.getClass().getName());

        DateTime timeoutTime = new DateTime().plusMillis(timeoutMs);
        while (timeoutTime.isAfterNow()) {
            try {

                runnable.run();

                return;

            } catch (Throwable e) {
                log.info(new DateTime().toString());
                log.info("Got exception: " + e);

                sleep(retryIntervalMs);
            } finally {
                log.info(new DateTime().toString());
            }
        }
        throw new RuntimeException("Running method failed after " + timeoutMs + " milliseconds.");

    }

    public static Object waitUntilMethodCallSucceedsAndReturnValue(Callable callable, int timeoutMs, int retryIntervalMs)
    {
        printMethodName();

        log.info("Method name: "+callable.getClass().getName());

        DateTime timeoutTime = new DateTime().plusMillis(timeoutMs);
        while (timeoutTime.isAfterNow()) {
            try {

                Object object = callable.call();

                return object;

            } catch (Throwable e) {
                log.info(new DateTime().toString());
                log.info("Got exception: " + e);

                sleep(retryIntervalMs);
            } finally {
                log.info(new DateTime().toString());
            }
        }
        throw new RuntimeException("Calling method failed after " + timeoutMs + " milliseconds.");

    }


}
