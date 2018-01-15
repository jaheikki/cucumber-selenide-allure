package microservice.browser;

import microservice.common.MsVariables;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class FirefoxLinuxGridDriverProvider extends BrowserBase{

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        return createSeleniumGridRemoteDriver("firefox","", Platform.LINUX, MsVariables.seleniumGridHubAddress);
    }
}

