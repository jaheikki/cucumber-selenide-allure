package teststepdefinitions;

import com.codeborne.selenide.Selenide;
import cucumber.api.java.en.And;
import cucumber_dependency_injection.World;
import microservice.common.MsVariables;
import microservice.helper.SFTPService;
import microservice.pages.ProductsPage;
import microservice.msrest.MsCatalogRest;

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

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(
            SFTPService.class.getName());

    @And("E-commerce Manager ui should be open")
    public void eCommerceManagerUiShouldBeOpen() {
        log.info(printMethodName());

        //world.msMainPage = Selenide.open(MsVariables.microserviceHost, ProductsPage.class);

    }

    @And("product (.*) should not be in the catalog through REST API")
    public void productShouldNotBeInTheCatalogThroughRESTAPI( String catalogItemName) {
        log.info(printMethodName());

        //MsCatalogRest.deleteCatalogItemByName(MsVariables.catalogServiceUrl, MsVariables.catalogURI,catalogItemName);
        MsCatalogRest.deleteCatalogItemByRestAssured(MsVariables.catalogServiceUrl, MsVariables.catalogURI,catalogItemName);
    }

    @And("product (.*) is added to the catalog with price (.*)")
    public void productIsAddedToTheCatalog(String catalogItem, String catalogItemPrice) {
        log.info(printMethodName());

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

