package microservice.browser;

import microservice.common.MsVariables;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Chrome61MacRemoteDriverProvider extends BrowserBase{

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        return createBSRemoteDriver("chrome","61",Platform.MAC, MsVariables.browserstackServer);
    }
}

