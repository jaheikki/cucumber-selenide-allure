package microservice.helper;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import microservice.common.MsVariables;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static java.lang.Thread.sleep;


public class SeleniumHelper {

    private static final Logger log = LoggerFactory.getLogger(SeleniumHelper.class);

    public static String getOneSubstringOutOfStringByRegexp(String regexp, String findText)  {
        printMethodName();

        //Pattern example: "Näytetään\\s(\\d+)\\s/\\s\\d+\\sliittymää"
        String retval = "";
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(findText);
        if (m.find()) {
            retval = m.group(1); //Regexp may contain one return block only
        }
        return retval;
    }

    /*Both xpath and cssSelector can be used as locator*/
    public static void myFindElementAndScrollWindow(String locator, int x, int y)  {
        log.info(printMethodName());
        WebDriver driver = getWebDriver();
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        final By seleniumBy = locator.startsWith("//") ? By.xpath(locator) : By.cssSelector(locator);

        $(seleniumBy).waitUntil(visible, 5000);
        $(seleniumBy).scrollTo();
        jse.executeScript("window.scrollBy(" + x + "," + y + ")", "");
    }

    public static void myFindSelenideElementAndScrollWindow(SelenideElement locator, int x, int y)  {
        log.info(printMethodName());
        WebDriver driver = getWebDriver();
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        locator.waitUntil(visible, 10000);
        locator.scrollTo();
        jse.executeScript("window.scrollBy(" + x + "," + y + ")", "");
    }

    public static void myClickElement(String xpath) {
        myClickElement(xpath, 0, -150);
    }

    public static void myClickElement(String xpath, int x, int y)  {
        try {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myFindElementAndScrollWindow(xpath, x, y);
            $(By.xpath(xpath)).click();

        } catch (WebDriverException webDriverException) {
            if (webDriverException.getMessage().contains("Element is not clickable at point")) {
                myFindElementAndScrollWindow(xpath, x, (y-50));
                $(By.xpath(xpath)).click();
            }
            else {
                throw webDriverException;
            }
        }
    }

    public static boolean myWaitUntilElementIsVisible(SelenideElement element, int timeoutMs)  {
        log.info(printMethodName());
        try {
            element.waitUntil(Condition.visible,timeoutMs);
            log.info("Found element: "+element.text());
            return true;

        } catch (Throwable e) {
            log.info("Could not find searched element.");
            return false;
        }
    }

    public static void myClick(String xpath) throws WebDriverException {
        $(By.xpath(xpath)).shouldBe(visible);
        $(By.xpath(xpath)).click();
    }

    public static void myDontHurryTooMuch()  {
        try {
            sleep(MsVariables.doNotHurryTooMuchDelayMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static String printMethodName() {
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        String line1 = ("\n*******************************************\n");
        String line2 = ("***** Method: " + methodName + " *****\n");
        String line3 = ("*******************************************\n");
        String line4 = (new DateTime()).toString();
        String printout = line1+line2+line3+line4;
        return printout;

    }


    public static String changeTimeFormatForFilenameUse()  {
        log.info(printMethodName());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("ddMMyyyykkmmss");
        String time = new DateTime().toString(fmt);
        log.info(time);
        log.info("Changed time format: " + time);
        return time;
    }

    public static String changeTimeFormatForUsernameUse()  {
        log.info(printMethodName());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("ddMMkkmmss");
        String time = new DateTime().toString(fmt);
        log.info(time);
        log.info("Changed time format: " + time);
        return time;
    }

}