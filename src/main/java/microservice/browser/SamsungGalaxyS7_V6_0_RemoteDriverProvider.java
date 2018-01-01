package microservice.browser;

import microservice.common.MsVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SamsungGalaxyS7_V6_0_RemoteDriverProvider extends BrowserBase{

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        return createBSRemoteMobileDriver("Samsung Galaxy S7","6.0", MsVariables.browserstackServer);
    }
}

