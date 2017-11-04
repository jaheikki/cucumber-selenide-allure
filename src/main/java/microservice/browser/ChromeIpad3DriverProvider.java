package microservice.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ChromeIpad3DriverProvider extends BrowserBase {
    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        //return createMobileChromeDriver("Apple iPad 3 / 4", desiredCapabilities);
        return createMobileChromeDriver("Apple iPad", desiredCapabilities);
    }
}
