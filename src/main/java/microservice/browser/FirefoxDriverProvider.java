package microservice.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxDriverProvider extends BrowserBase {
    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        //return setCustomWindowSize(new FirefoxDriver(desiredCapabilities),0,0); //0,0 means start maximized
        FirefoxProfile profile = new FirefoxProfile();
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");
        profile.setAcceptUntrustedCertificates(true); // Accept Untrusted Certificates in firefox always with this class
        desiredCapabilities.setCapability("marionette", true);
        desiredCapabilities.setCapability(FirefoxDriver.PROFILE, profile);
        return new FirefoxDriver(desiredCapabilities);
    }
}
