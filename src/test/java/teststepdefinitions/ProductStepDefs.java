package teststepdefinitions;

import com.codeborne.selenide.Selenide;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber_dependency_injection.World;
import microservice.common.MsVariables;
import microservice.pages.ProductsPage;
import microservice.msrest.MsCatalogRest;
import microservice.msrest.MsCustomerRest;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static microservice.helper.SeleniumHelper.printMethodName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ProductStepDefs {

    private ProductsPage mainPage;
    private World world;

    public ProductStepDefs(World world) {
        this.world = world;

    }

    @And("E-commerce Manager ui should be open")
    public void eCommerceManagerUiShouldBeOpen() {
        printMethodName();

        world.msMainPage = Selenide.open(MsVariables.microserviceHost, ProductsPage.class);

    }

    @And("product (.*) should not be in the catalog through REST API")
    public void productShouldNotBeInTheCatalogThroughRESTAPI( String catalogItemName) {
        printMethodName();

        MsCatalogRest.deleteCatalogItemByName(MsVariables.catalogServiceUrl, MsVariables.catalogURI,catalogItemName);

    }


    @And("product (.*) is added to the catalog with price (.*)")
    public void productIsAddedToTheCatalog(String catalogItem, String catalogItemPrice) {
        printMethodName();

        world.msMainPage.navigateToAddProductPage()
                .addCatalogItem(catalogItem, catalogItemPrice)
                .navigateBackToProductsPage();
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
//
//    @Then("I can verify my order of (.*) with price (.*) by customer (.*)")
//    public void iCanVerifyMyOrder(String catalogItem, String price,String customer) {
//        printMethodName();
//
//        world.msMainPage.navigateToOrdersPage()
//                .verifyOrderByCustomer(customer,catalogItem, price)
//                .navigateBackToProductsPage();
//
//    }
}

