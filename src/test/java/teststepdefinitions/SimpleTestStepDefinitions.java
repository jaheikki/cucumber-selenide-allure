package teststepdefinitions;

import cucumber_dependency_injection.World;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static microservice.helper.SeleniumHelper.printMethodName;

public class SimpleTestStepDefinitions {

    private static final Logger log = LoggerFactory.getLogger(SimpleTestStepDefinitions.class);

    private World world;

    public SimpleTestStepDefinitions(World world) {
        this.world = world;

    }

    @Given("^browser should be open in Wikipedia page$")
    public void browserShouldBeOpenWikipedia() {
        log.info(printMethodName());

        Selenide.open("https://en.wikipedia.org/wiki/Main_Page");
        $(By.xpath("//a[contains(@title,'Visit the main page')]")).shouldBe(Condition.visible);
        Selenide.sleep(500);

    }

    @When("^i write (.*) to search field$")
    public void iWriteBrowserstackToSearchField(String searchWord) {
        log.info(printMethodName());

        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).val(searchWord);
        Selenide.sleep(500);

    }

    @And("^i press ENTER$")
    public void iPressEnter() {
        log.info(printMethodName());

        $(By.xpath("//input[contains(@placeholder,'Search Wikipedia')]")).shouldBe(Condition.visible).pressEnter();
        Selenide.sleep(500);
    }

    @Then("^i am able to see BrowserStack wikipedia page$")
    public void iAmAbleToSeeBrowserStackWikipediaPage() {
        log.info(printMethodName());

        $(By.xpath("//h1[contains(text(),'BrowserStack')]")).shouldBe(Condition.visible);
        Selenide.sleep(2000);

    }

}
