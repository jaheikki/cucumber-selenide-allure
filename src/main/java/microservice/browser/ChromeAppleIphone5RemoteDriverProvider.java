package microservice.browser;

import microservice.common.MsVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class ChromeAppleIphone5RemoteDriverProvider extends BrowserBase {

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        return createMobileChromeRemoteDriver("Apple iPhone 5", MsVariables.seleniumGridHubAddress, desiredCapabilities);
    }
}
