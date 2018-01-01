package microservice.browser;

import com.browserstack.local.Local;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverProvider;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class BrowserBase implements WebDriverProvider{


    public static WebDriver createBSRemoteDriver(String browser, String browserVersion, Platform platform, String bsHubServer)
    {
        Local l;

        String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");

        String hubUrl = "https://"+System.getenv("BROWSERSTACK_USERNAME")+":"+System.getenv("BROWSERSTACK_ACCESS_KEY")+"@"+bsHubServer;

        DesiredCapabilities capability = new DesiredCapabilities(browser, browserVersion, platform);
        capability.setCapability("browserstack.debug", true);
        capability.setCapability("acceptSslCerts", true);

        if (System.getProperty("local") != null && System.getProperty("local").equals("true")) {
            capability.setCapability("browserstack.local", "true");
            l = new Local();
            Map<String, String> options = new HashMap<String, String>();
            options.put("key", ACCESS_KEY);
            try {
                l.start(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            return new RemoteWebDriver(new URL(hubUrl), capability);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static WebDriver createBSRemoteMobileDriver(String mobile, String mobileOsVersion, String bsHubServer)
    {
        Local l;

        String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");

        String hubUrl = "https://"+System.getenv("BROWSERSTACK_USERNAME")+":"+System.getenv("BROWSERSTACK_ACCESS_KEY")+"@"+bsHubServer;

        DesiredCapabilities capability = new DesiredCapabilities();

        capability.setCapability("realMobile", true);
        capability.setCapability("device", mobile);
        capability.setCapability("os_version", mobileOsVersion);
        capability.setCapability("browserstack.debug", true);
        capability.setCapability("acceptSslCerts", true);

        if (System.getProperty("local") != null && System.getProperty("local").equals("true")) {
            capability.setCapability("browserstack.local", "true");
            l = new Local();
            Map<String, String> options = new HashMap<String, String>();
            options.put("key", ACCESS_KEY);
            try {
                l.start(options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            return new RemoteWebDriver(new URL(hubUrl), capability);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static WebDriver createSeleniumGridRemoteDriver(String browser, String browserVersion, Platform platform, String hubUrl)
    {
        DesiredCapabilities capability = new DesiredCapabilities(browser, browserVersion, platform);

        try {
            return new RemoteWebDriver(new URL(hubUrl), capability);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
