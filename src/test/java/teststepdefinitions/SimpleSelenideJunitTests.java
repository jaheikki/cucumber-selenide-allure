//package teststepdefinitions;
//
//import com.codeborne.selenide.Condition;
//import com.codeborne.selenide.Selenide;
//import org.junit.Test;
//import org.openqa.selenium.By;
//
//import static com.codeborne.selenide.Selenide.$;
//import static microservice.helper.SeleniumHelper.printMethodName;
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
//        Selenide.sleep(500);
//
//        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).val(searchWord);
//        Selenide.sleep(500);
//
//        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).pressEnter();
//        Selenide.sleep(500);
//
//        $(By.xpath("//h1[contains(text(),'BrowserStack')]")).shouldBe(Condition.visible);
//        Selenide.sleep(2000);
//
//    }
//
//}
