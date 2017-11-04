package microservice.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class IEDriverProvider extends BrowserBase {
    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        return new InternetExplorerDriver(desiredCapabilities);
    }
}
