package microservice.browser;

import microservice.common.MsVariables;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class IPhone7_V10_0_RemoteDriverProvider extends BrowserBase{

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        return createBSRemoteMobileDriver("iPhone 7","10.0", MsVariables.browserstackServer);
    }
}

