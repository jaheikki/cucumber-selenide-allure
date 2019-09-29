package teststepdefinitions;

import com.codeborne.selenide.Selenide;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber_dependency_injection.World;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import microservice.common.MsVariables;
import microservice.msrest.MsCatalogRest;
import microservice.msrest.MsCustomerRest;
import microservice.pages.ProductsPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static microservice.helper.SeleniumHelper.printMethodName;


public class CustomerStepDefs {

    private static final Logger log = LoggerFactory.getLogger(CustomerStepDefs.class);

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

    @And("customer (.*) having email (.*) should not exist through REST API")
    public void customerShouldNotExistThroughRESTAPI(String customer, String customerEmail) {
        log.info(printMethodName());

        //By Jersey & Jackson
        //MsCustomerRest.deleteCustomerByName(MsVariables.customerServiceUrl, MsVariables.customerURI,customer);

        //By Rest Assured
        RestAssured.baseURI = MsVariables.customerServiceUrl;

        while (true) {
            Response response = given().get( MsVariables.customerURI).then().log().ifError().extract().response();

            //Get customer entry id by searching by email
            Integer id = response.path("_embedded.customer.find { it.email == '" + customerEmail + "' }.id");


            if (id == null) {
                log.info("No customer by email " + customerEmail + " found.");
                break;
            }

            given().delete( MsVariables.customerURI + "/" + id).then().log().ifError().statusCode(204).log().all();

            log.info("Customer by email " + customerEmail + " deleted.");

        }
    }

    @And("customer (.*) (.*) is added")
    public void customerIsAdded(String firstname, String lastname) {
        log.info(printMethodName());

        world.msMainPage.navigateToCustomersPage()
                .addCustomer(firstname,lastname)
                .navigateBackToProductsPage();
    }
}

