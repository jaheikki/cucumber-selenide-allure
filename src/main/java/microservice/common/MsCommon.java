package microservice.common;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import microservice.helper.SeleniumHelper;
import org.joda.time.DateTime;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static microservice.common.MsConstants.*;
import static microservice.helper.SeleniumHelper.printMethodName;

public class MsCommon {

    public static void setUp() throws Exception {

        // default folder not good
        //Configuration.reportsFolder = OnlinefirstConstants.onlinefirstSelenideReportsFolder;

        // default timeout (env might be very slow)
        //Configuration.timeout = MsConstants.commonSelenideTimeout;

        //If true, Selenide uses Javascript to click element -> can be used as temporary workaround if bugs related clicking (especially in Chrome)
        //Configuration.clickViaJs = selenideClickElementByJavascript;

        //Prevent selenide closing browser, because we want to logout OE before closing browser
        //See: logoutAndCloseBrowser()
        //Configuration.holdBrowserOpen=true;

        //Configuration.browser="firefox";
        Configuration.browser="chrome";

    }

    public static void waitForElementClick(String clickLocator, String verifyLocator)  {
        printMethodName();

        try {
            waitForElementClick(elementClickTimeoutMs, elementClickRetryIntervalMs, elementClickXpathWaitTimeoutMs, elementClickAngularWaitTimeoutMs, -150, false, clickLocator, verifyLocator, doRefreshOnFailure);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void waitForElementClick(String clickLocator, String verifyLocator, boolean isAngular)  {
        printMethodName();

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
        printMethodName();

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
                        System.out.println("Current selenide timeout ms: " +Configuration.timeout);
                        verifyElement(verifyLocator);
                    }
                } else {
                    System.out.println("Already in correct page: "+verifyLocator);
                }
                return;
            } catch (Throwable e) {
                System.out.println(new DateTime());
                System.out.println("Got exception: " + e);
                System.out.println("Sleeping " + (retryIntervalMs) + " milliseconds");
                sleep(retryIntervalMs);
                if (doRefreshOnFailure) Selenide.refresh();
            } finally {
                System.out.println(new DateTime());
                Configuration.timeout = MsConstants.commonSelenideTimeout; //force return original configuration timeout
            }
        }
        throw new RuntimeException("Element click failed after " + timeoutMs + " milliseconds.");
    }

    /* Normally scrolled only with y-axis direction, e.g. set yAxisPixelsMove = -150 means scrolling little above the element */
    public static void scrollNearElement(int xAxisPixelsMove,int yAxisPixelsMove, String clickLocator)  {
        printMethodName();

        if (xAxisPixelsMove != 0 || yAxisPixelsMove != 0)   {
            System.out.println("Now scrolling near element");
            SeleniumHelper.myFindElementAndScrollWindow(clickLocator, xAxisPixelsMove, yAxisPixelsMove);
        }
    }

    /*Can be used with xpath and cssSelector*/
    public static void clickElement(String clickLocator) {
        printMethodName();

        System.out.println("About to click element: " + clickLocator);

        if(clickLocator.startsWith("//")) {
            $(By.xpath(clickLocator)).shouldBe(visible).shouldBe(enabled).click();

        } else {
            $(By.cssSelector(clickLocator)).shouldBe(visible).shouldBe(enabled).click();
        }
        System.out.println("Click done.");
    }

    /*Can be used with xpath and cssSelector*/
    public static void verifyElement(String verifyLocator) {
        printMethodName();

        System.out.println("Waiting for verifyLocator: " + verifyLocator);

        if(verifyLocator.contains("//")) {
            $(By.xpath(verifyLocator)).shouldBe(visible);

        } else {
            $(By.cssSelector(verifyLocator)).shouldBe(visible);
        }

        System.out.println("Waiting for verifyLocator done.");
    }

    /*Can be used with xpath and cssSelector*/
    public static void verifyElementNotVisible(String verifyLocator) {
        printMethodName();

        System.out.println("Waiting for verifyLocator not visible: " + verifyLocator);

        if(verifyLocator.contains("//")) {
            $(By.xpath(verifyLocator)).shouldNotBe(visible);

        } else {
            $(By.cssSelector(verifyLocator)).shouldNotBe(visible);
        }

        System.out.println("Waiting for verifyLocator done.");
    }


    public static void waitforAngularJS() {

        waitforAngularJS(elementClickAngularWaitTimeoutMs);
    }

    public static void waitforAngularJS(int timeoutInMs) {

        int timeout = timeoutInMs/1000; //convert to seconds

        DateTime date;

        System.out.println("*** waitforAngularJS() start ***");
        System.out.println(new DateTime());

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
                    System.out.println("noAjaxRequests: "+noAjaxRequests.toString());
                    return "1".equals(noAjaxRequests.toString());

                }
            });
        } catch (Throwable e) {
            String ErrorMsg =
                    "Failed wait for no angular JS request within timeout of : " + timeout + " seconds  at url:"
                            + driver.getCurrentUrl();
            System.out.println(ErrorMsg);
        }
        System.out.println("*** waitforAngularJS() end ***");
        System.out.println(new DateTime());


    }

    public static void navigateBackToMsMainPage() {
        printMethodName();

        waitForElementClick("//a[contains(text(),'Home')]","");

    }

}
