package teststepdefinitions;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber_dependency_injection.World;
import microservice.pages.ProductsPage;

import java.io.IOException;

import static microservice.helper.SeleniumHelper.printMethodName;


public class OrderStepDefs {

    private ProductsPage mainPage;
    private World world;

    public OrderStepDefs(World world) {
        this.world = world;

    }

    @Given("^order by (.*) should not exist$")
    public void orderShouldNotExistByCustomer(String customer) throws IOException {
        printMethodName();

        //System.out.println("Sleeping before opening browser...");
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
        printMethodName();

        //Remove this, just to get UI test to fail for a allure report and selenide screenshot
        //$(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);

        world.msMainPage.navigateToOrdersPage()
                .addOrderByCustomer(catalogItem, customer)
                .navigateBackToProductsPage();

    }

    @Then("I can verify my order of (.*) with price (.*) by customer (.*)")
    public void iCanVerifyMyOrder(String catalogItem, String price,String customer) {
        printMethodName();

        world.msMainPage.navigateToOrdersPage()
                .verifyOrderByCustomer(customer,catalogItem, price)
                .navigateBackToProductsPage();

    }
}

