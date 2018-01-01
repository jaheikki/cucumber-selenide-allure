package microservice.browser;

import microservice.common.MsVariables;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Firefox53WindowsRemoteDriverProvider extends BrowserBase{

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        return createBSRemoteDriver("firefox","53",Platform.WINDOWS, MsVariables.browserstackServer);
    }
}

