package microservice.helper;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.switchTo;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BrowserHelpper implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(BrowserHelpper.class);

    private WebDriver driver;
    private final String parentHandle;
    private String newHandle;
    private JavascriptExecutor jsExecutor;



    public  BrowserHelpper() {
        driver = getWebDriver();
        parentHandle = driver.getWindowHandle(); // get the current (parent) window handle
        jsExecutor = (JavascriptExecutor) driver;
    }

    public void openNewTabAndSwitch() throws Exception {
        if (hasOpen()){
            jsExecutor.executeScript("window.open()");
            switchToNewWindow();
        }
        else throw new Exception("Cannot open a new tab. Is the Browser open?");
    }

    public void switchToNewWindow() throws InterruptedException {
        boolean notFound=true;
        int counter=0;
        while(notFound && counter<10) {
            for (String winHandle : driver.getWindowHandles()) {
                if (!winHandle.equals(parentHandle)) {
                    driver.switchTo().window(winHandle); // switch focus of WebDriver to the next found window handle (that's your newly opened window)
                    this.newHandle = winHandle;
                    notFound=false;
                }
                else {
                    log.info("No new window found yet! Keep waiting");
                    Thread.sleep(1000);
                    counter++;
                }
            }
        }
        if (notFound) throw new InterruptedException("Cannot find a new windows!");
    }

    public void closeTheNewWindowAndGoBackMain() {
        if (driver.getWindowHandle().equals(newHandle)){
            driver.close(); //close new browser window
            switchTo().window(parentHandle); //switch to main window
        }
        else {
            log.info("No new window found for closing. Maybe already closed.");
        }
    }

    private boolean hasOpen() {
        return ((RemoteWebDriver)driver).getSessionId() != null;
    }

    @Override
    public void close() throws IOException {
        closeTheNewWindowAndGoBackMain();
    }
}
