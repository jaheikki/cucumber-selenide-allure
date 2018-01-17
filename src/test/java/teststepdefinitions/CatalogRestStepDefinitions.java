package teststepdefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import microservice.common.MsVariables;
import microservice.msrest.MsCatalogRest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class CatalogRestStepDefinitions {

    private static JsonNode catalogItem;

    @Given("^catalog item exists at the database$")
    public void catalogItemExistsAtTheDatabase() throws InterruptedException {

    }

    @When("i get the catalog item from rest")
    public void iGetTheCatalogItemFromRest() {


        //System.out.println("Sleeping due to check if services are up...");
        //Selenide.sleep(30000);
        //catalogItem = MsCatalogRest.getSingleCatalogItemWithId(MsVariables.catalogServiceUrl, MsVariables.catalogURI, "2");
        catalogItem = MsCatalogRest.waitForGetSingleCatalogItemWithId(60,5, MsVariables.catalogServiceUrl, MsVariables.catalogURI, "2");
    }

    @Then("catalog item name should be (.*)")
    public void catalogItemNameShouldBe(String catalogItemName) {

        assertThat((catalogItem.get("name").asText()), is(catalogItemName));
        System.out.println("Catalog item name was OK: "+catalogItemName);

    }

    @And("catalog item price should be (.*)")
    public void catalogItemPriceShouldBe(String catalogItemPrice) {

        assertThat((catalogItem.get("price").asText()), is(catalogItemPrice));
        System.out.println("Catalog item price was OK: "+catalogItemPrice);

    }
}


