package teststepdefinitions;

import com.codeborne.selenide.Selenide;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber_dependency_injection.World;
import microservice.common.MsVariables;
import microservice.msrest.MsCatalogRest;
import microservice.msrest.MsCustomerRest;
import microservice.pages.ProductsPage;

import java.io.IOException;

import static microservice.helper.SeleniumHelper.printMethodName;


public class CustomerStepDefs {

    private ProductsPage mainPage;
    private World world;

    public CustomerStepDefs(World world) {
        this.world = world;

    }

//    @When("I order product (.*) as customer (.*)")
//    public void iOrderProduct(String catalogItem, String customer) {
//        printMethodName();
//
//        //Remove this, just to get UI test to fail for a allure report and selenide screenshot
//        //$(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);
//
//        world.msMainPage.navigateToOrdersPage()
//                .addOrderByCustomer(catalogItem, customer)
//                .navigateBackToProductsPage();
//
//    }

//    @Then("I can verify my order of (.*) with price (.*) by customer (.*)")
//    public void iCanVerifyMyOrder(String catalogItem, String price,String customer) {
//        printMethodName();
//
//        world.msMainPage.navigateToOrdersPage()
//                .verifyOrderByCustomer(customer,catalogItem, price)
//                .navigateBackToProductsPage();
//
//    }

    @And("customer (.*) should not exist through REST API")
    public void customerShouldNotExistThroughRESTAPI(String customer) {
        printMethodName();

        MsCustomerRest.deleteCustomerByName(MsVariables.customerServiceUrl, MsVariables.customerURI,customer);

    }

    @And("customer (.*) (.*) is added")
    public void customerIsAdded(String firstname, String lastname) {
        printMethodName();

        world.msMainPage.navigateToCustomersPage()
                .addCustomer(firstname,lastname)
                .navigateBackToProductsPage();
    }
}

