//package junit;
//
//import com.codeborne.selenide.*;
//import org.junit.Test;
//import org.openqa.selenium.*;
//import org.openqa.selenium.interactions.Actions;
//
//import javax.xml.xpath.XPath;
//
//import java.util.Iterator;
//import java.util.List;
//
//import static com.codeborne.selenide.Selenide.$;
//import static com.codeborne.selenide.Selenide.$$;
//import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
//import static microservice.helper.SeleniumHelper.printMethodName;
//
//import static org.junit.Assert.assertTrue;
//
//public class SimpleSelenideJunitTests {
//
//    @Test
//    public void wikipediaTest() {
//
//        String searchWord = "browserstack";
//
//        Selenide.open("https://en.wikipedia.org/wiki/Main_Page");
//
//        $(By.xpath("//a[contains(@title,'Visit the main page')]")).shouldBe(Condition.visible);
//
//        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).val(searchWord);
//        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).val(searchWord);
//
//        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).pressEnter();
//
//        $(By.xpath("//h1[contains(text(),'BrowserStack2')]")).shouldBe(Condition.visible); //for test Selenide screenshots
////        $(By.xpath("//h1[contains(text(),'BrowserStack')]")).shouldBe(Condition.visible);
//        Selenide.sleep(2000);
//
//    }
//
//    @Test
//    public void wikipediaCollectionTest() {
//
//        String searchWord = "browserstack";
//
//        Selenide.open("https://en.wikipedia.org/wiki/Main_Page");
//
//        //Navigate to Contents page
//        ElementsCollection menu = $$(By.xpath("//div[contains(@class,'body')]//ul"));
//        menu
//                .find(Condition.text("Current events"))
//                .scrollTo()
//                .hover()
//                .click();
//
//        $(By.xpath("//h1[contains(text(),'Portal:Current events')]")).shouldBe(Condition.visible);
//
//    }
//
//    @Test
//    public void wikipediaIterationTest() {
//
//        String searchWord = "browserstack";
//
//        Selenide.open("https://en.wikipedia.org/wiki/Main_Page");
//
//        ElementsCollection navigationElements = $$(By.xpath("//table[contains(@role,'presentation')]//tr"));
//        Iterator<SelenideElement> iter = navigationElements.iterator();
//
//        while (iter.hasNext()) { //iterate over rows (tr)
//            SelenideElement element = iter.next().shouldBe(Condition.visible);
//            if (element.$(By.cssSelector("#mp-right")).isDisplayed()) { //find right cell (td)
//                element.find(By.cssSelector("#In_the_news")).shouldHave(Condition.text("In the news")); //verify element contains correct text
//            }
//        }
//    }
//
//    @Test
//    public void seleniumThroughSelenideTest() {
//
//        String searchWord = "browserstack";
//
//        Selenide.open("https://en.wikipedia.org/wiki/Main_Page");
//
//        List<WebElement> rows = getWebDriver().findElements(By.xpath("//table[contains(@role,'presentation')]//tr"));
//
//        try {
//            for (WebElement row:rows) {
//
//                if (row.findElement(By.cssSelector("#mp-right")).isDisplayed()) {
//                    String text = row.findElement(By.cssSelector("#In_the_news")).getText();
//                    assertTrue(text.contains("In the news"));
//                }
//            }
//        } catch (WebDriverException e) {
//            log.info("#mp-right cannot be found from second row, so therefore exception needs to catched: "+e);
//        }
//
//    }
//
//}
