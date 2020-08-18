package teststepdefinitions;

import cucumber_dependency_injection.World;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import microservice.pages.ProductsPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static microservice.helper.SeleniumHelper.printMethodName;


public class OrderStepDefs {

    private static final Logger log = LoggerFactory.getLogger(OrderStepDefs.class);

    private ProductsPage mainPage;
    private World world;

    public OrderStepDefs(World world) {
        this.world = world;

    }

    @Given("^order by (.*) should not exist$")
    public void orderShouldNotExistByCustomer(String customer) throws IOException {
        log.info(printMethodName());

        //log.info("Sleeping before opening browser...");
        //Selenide.sleep(120000);

        //Enable this if "Whitelabel Error Page" caused by order service down, will stop and start Microservice
//        try {
//            HashMap<String, ArrayList<String>> out1 = MsCommon.executeSystemCommandAndReturnStdOutAndStdErr("docker-compose -f /Users/heikmjar/git/microservice-demo-acceptance-tests-copy/src/test/resources/docker-compose-dev.yml down -v --remove-orphans", true);
//            HashMap<String, ArrayList<String>> out2 = MsCommon.executeSystemCommandAndReturnStdOutAndStdErr("docker-compose -f /Users/heikmjar/git/microservice-demo-acceptance-tests-copy/src/test/resources/docker-compose-dev.yml up -d",true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        world.msMainPage.navigateToOrdersPage()
                .deleteOrderByCustomer(customer)
                .navigateBackToProductsPage();

    }

    @When("I order product (.*) as customer (.*)")
    public void iOrderProduct(String catalogItem, String customer) {
        log.info(printMethodName());

        //Remove this, just to get UI test to fail for a allure report and selenide screenshot
        //$(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);

        world.msMainPage.navigateToOrdersPage()
                .addOrderByCustomer(catalogItem, customer)
                .navigateBackToProductsPage();

    }

//    @Then("I can verify my order of (.*) with price (.*) by customer (.*)")
//    public void iCanVerifyMyOrder(String catalogItem, String price,String customer) {
//        log.info(printMethodName());
//
//        world.msMainPage.navigateToOrdersPage()
//                .verifyOrderByCustomer(customer,catalogItem, price)
//                .navigateBackToProductsPage();
//
//    }

    @Then("I can verify (.*) order with details:")
    public void iCanVerifyMyOrder(String customer, Map<String,String> catalogDetails) {
        log.info(printMethodName());

        //get's only first key-value pair from map (for testing DataTable with single line only)
        Map.Entry<String,String> entry = catalogDetails.entrySet().iterator().next();
        String catalogItem = entry.getKey();
        String price = entry.getValue();

        world.msMainPage.navigateToOrdersPage()
                .verifyOrderByCustomer(customer, catalogItem, price)
                .navigateBackToProductsPage();

    }
}

