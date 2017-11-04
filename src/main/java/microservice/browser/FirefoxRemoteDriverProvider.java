package microservice.browser;

import microservice.common.MsConstants;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/*Note: To be able to use this custom browser you need to have a windows machine (e.g. virtual machine) and you need to setup:

SETUP HUB e.g.:
java -jar selenium-server-standalone-2.44.0.jar -role hub

SETUP NODE e.g.:
java -jar selenium-server-standalone-2.44.0.jar -role node -browser browserName=ie,version=11.0,platform=WINDOWS -hub http://localhost:4444/grid/console -Dwebdriver.ie.driver=c:\temp\IEDriverServer.exe

*/
public class FirefoxRemoteDriverProvider extends BrowserBase{

    @Override
    public WebDriver createDriver(DesiredCapabilities desiredCapabilities) {
        return createSeleniumGridRemoteDriver("firefox","",Platform.ANY, MsConstants.seleniumGridHubAddress);
    }
}

