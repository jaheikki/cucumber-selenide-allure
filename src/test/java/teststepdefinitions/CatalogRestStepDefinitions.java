package teststepdefinitions;

import com.fasterxml.jackson.databind.JsonNode;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import microservice.common.MsVariables;
import microservice.helper.SeleniumHelper;
import microservice.msrest.MsCatalogRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class CatalogRestStepDefinitions {

    private static final Logger log = LoggerFactory.getLogger(CommonStepDefinitions.class);

    private static JsonNode catalogItem;

    @Given("^catalog item exists at the database$")
    public void catalogItemExistsAtTheDatabase() throws InterruptedException {
        log.info(SeleniumHelper.printMethodName());

    }

    @When("i get the catalog item from rest")
    public void iGetTheCatalogItemFromRest() {


        //log.info("Sleeping due to check if services are up...");
        //Selenide.sleep(30000);
        //catalogItem = MsCatalogRest.getSingleCatalogItemWithId(MsVariables.catalogServiceUrl, MsVariables.catalogURI, "2");
        catalogItem = MsCatalogRest.waitForGetSingleCatalogItemWithId(60,5, MsVariables.catalogServiceUrl, MsVariables.catalogURI, "2");
    }

    @Then("catalog item name should be (.*)")
    public void catalogItemNameShouldBe(String catalogItemName) {

        assertThat((catalogItem.get("name").asText()), is(catalogItemName));
        log.info("Catalog item name was OK: "+catalogItemName);

    }

    @And("catalog item price should be (.*)")
    public void catalogItemPriceShouldBe(String catalogItemPrice) {

        assertThat((catalogItem.get("price").asText()), is(catalogItemPrice));
        log.info("Catalog item price was OK: "+catalogItemPrice);

    }
}


