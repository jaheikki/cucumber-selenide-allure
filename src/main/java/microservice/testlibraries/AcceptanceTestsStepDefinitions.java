package microservice.testlibraries;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import microservice.common.MsConstants;
import microservice.pages.MsMainPage;
import microservice.msrest.MsCatalogRest;
import microservice.msrest.MsCustomerRest;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static microservice.helper.SeleniumHelper.printMethodName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class AcceptanceTestsStepDefinitions {

    private MsMainPage msMainPage;

    @Given("^order by (.*) should not exist$")
    public void orderShouldNotExistByCustomer(String customer) {
        printMethodName();

        //System.out.println("Sleeping before opening browser...");
        //Selenide.sleep(120000);
        msMainPage = Selenide.open(MsConstants.microserviceHost, MsMainPage.class);
        msMainPage.navigateToOrderPage()
                .deleteOrderByCustomer(customer)
                .navigateBackToMainPage();
    }

    @And("product (.*) should not be in the catalog through REST API")
    public void productShouldNotBeInTheCatalogThroughRESTAPI( String catalogItemName) {
        printMethodName();

        MsCatalogRest.deleteCatalogItemByName(MsConstants.catalogServiceUrl,MsConstants.catalogURI,catalogItemName);

    }

    @And("customer (.*) should not exist through REST API")
    public void customerShouldNotExistThroughRESTAPI(String customer) {
        printMethodName();

        MsCustomerRest.deleteCustomerByName(MsConstants.customerServiceUrl,MsConstants.customerURI,customer);

    }

    @And("product (.*) is added to the catalog with price (.*)")
    public void productIsAddedToTheCatalog(String catalogItem, String catalogItemPrice) {
        printMethodName();

        msMainPage.navigateToCatalogPage()
                .addCatalogItem(catalogItem, catalogItemPrice)
                .navigateBackToMainPage();
    }

    @And("customer (.*) is added")
    public void customerIsAdded(String customer) {
        printMethodName();

        msMainPage.navigateToCustomerPage()
                .addCustomer(customer)
                .navigateBackToMainPage();
    }

    @When("I order product (.*) as customer (.*)")
    public void iOrderProduct(String catalogItem, String customer) {
        printMethodName();

        //Remove this, just to get UI test to fail for a allure report and selenide screenshot
        //$(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);

        msMainPage.navigateToOrderPage()
                .addOrderByCustomer(catalogItem, customer)
                .navigateBackToMainPage();

    }

    @Then("I can verify my order of (.*) with price (.*) by customer (.*)")
    public void iCanVerifyMyOrder(String catalogItem, String price,String customer) {
        printMethodName();

        msMainPage.navigateToOrderPage()
                .verifyOrderByCustomer(customer,catalogItem, price)
                .navigateBackToMainPage();


    }
}

