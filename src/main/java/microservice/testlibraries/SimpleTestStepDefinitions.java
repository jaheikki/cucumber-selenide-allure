package microservice.testlibraries;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.By;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static microservice.helper.SeleniumHelper.printMethodName;

public class SimpleTestStepDefinitions {

    @Given("^browser should be open Wikipedia$")
    public void browserShouldBeOpenWikipedia() throws IOException {
        printMethodName();

        Selenide.open("https://en.wikipedia.org/wiki/Main_Page");
        $(By.xpath("//a[contains(@title,'Visit the main page')]")).shouldBe(Condition.visible);
        Selenide.sleep(2000);

    }

    @When("^i write (.*) to search field$")
    public void iWriteBrowserstackToSearchField(String searchWord) {
        printMethodName();

        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).val(searchWord);
        Selenide.sleep(1000);

    }

    @And("^i press ENTER$")
    public void iPressEnter() {
        printMethodName();

        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).pressEnter();
        Selenide.sleep(1000);
    }

    @Then("^i am able to see BrowserStack wikipedia page$")
    public void iAmAbleToSeeBrowserStackWikipediaPage() {
        printMethodName();

        $(By.xpath("//h1[contains(text(),'BrowserStack')]")).shouldBe(Condition.visible);
        Selenide.sleep(2000);

    }

}
