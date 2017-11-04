package microservice.browser;

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

    public static WebDriver createMobileChromeDriver(final String deviceName, DesiredCapabilities desiredCapabilities) {
        Map<String, String> mobileEmulation = new HashMap<String, String>();
        mobileEmulation.put("deviceName", deviceName);
        Map<String, Object> chromeOptions = new HashMap<String, Object>();
        chromeOptions.put("mobileEmulation", mobileEmulation);
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);
        return new ChromeDriver(desiredCapabilities);

    }

    public static WebDriver createMobileChromeRemoteDriver(final String deviceName,String hubUrl,DesiredCapabilities desiredCapabilities) {
        Map<String, String> mobileEmulation = new HashMap<String, String>();
        mobileEmulation.put("deviceName", deviceName);
        Map<String, Object> chromeOptions = new HashMap<String, Object>();
        chromeOptions.put("mobileEmulation", mobileEmulation);
        desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        desiredCapabilities.setCapability("browserName","chrome");
        desiredCapabilities.setCapability("version","");
        desiredCapabilities.setCapability("platform","ANY");
        desiredCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS,true);

        try {
            return new RemoteWebDriver(new URL(hubUrl), desiredCapabilities);
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

    public static WebDriver setCustomWindowSize(WebDriver webDriver, int windowWidth, int windowHeight) {

        if (windowWidth == 0 || windowHeight == 0) {

//            java.awt.Point targetPosition = new java.awt.Point(0, 0);
//            webDriver.manage().window().setPosition(targetPosition);
//
//            java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//            windowWidth = (int)screenSize.getWidth();
//            windowHeight = (int)screenSize.getHeight();
//
//            java.awt.Dimension targetSize = new java.awt.Dimension(windowWidth, windowHeight); //your screen resolution here
//            webDriver.manage().window().setSize(targetSize);

        } else {

            webDriver.manage().window().setPosition(new Point(0,0));
            webDriver.manage().window().setSize(new Dimension(windowWidth,windowHeight));
        }
        return webDriver;
    }

}
