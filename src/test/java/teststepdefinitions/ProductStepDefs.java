package teststepdefinitions;

import com.codeborne.selenide.Selenide;
import cucumber_dependency_injection.World;
import io.cucumber.java.en.And;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import microservice.common.MsVariables;
import microservice.helper.SFTPService;
import microservice.pages.ProductsPage;
import microservice.msrest.MsCatalogRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static io.restassured.RestAssured.given;
import static microservice.helper.SeleniumHelper.printMethodName;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ProductStepDefs {

    private ProductsPage mainPage;
    private World world;

    public ProductStepDefs(World world) {
        this.world = world;

    }

    private static final Logger log = LoggerFactory.getLogger(ProductStepDefs.class);

    @And("E-commerce Manager ui should be open")
    public void eCommerceManagerUiShouldBeOpen() {
        log.info(printMethodName());

        world.msMainPage = Selenide.open(MsVariables.microserviceHost, ProductsPage.class);

    }

    @And("product (.*) should not be in the catalog through REST API")
    public void productShouldNotBeInTheCatalogThroughRESTAPI( String catalogItemName) {
        log.info(printMethodName());

        //Delete by Jersey and Jackson libs
        //MsCatalogRest.deleteCatalogItemByName(MsVariables.catalogServiceUrl, MsVariables.catalogURI,catalogItemName);

        //By Rest Assured
        RestAssured.baseURI = MsVariables.catalogServiceUrl;
        log.debug("RestAssured.baseURI: "+RestAssured.baseURI);

        //Looping to delete all duplicate (if exist) catalogitems
        while (true) {
            Response response  = given().get(MsVariables.catalogURI).then().log().ifError().extract().response();

            Integer id = response.path("_embedded.catalog.find { it.name == '"+catalogItemName+"' }.id");

            if (id == null) {
                log.info("No catalog item "+catalogItemName+" found.");
                break;
            }

            given().delete (MsVariables.catalogURI +"/"+id).then().log().ifError().statusCode(204).log().all();

            log.info("Catalog item "+catalogItemName+" deleted.");
        }

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

