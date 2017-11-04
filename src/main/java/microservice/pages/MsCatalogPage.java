package microservice.pages;

import com.codeborne.selenide.Condition;
import microservice.common.MsCommon;
import microservice.helper.SeleniumHelper;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static microservice.helper.SeleniumHelper.printMethodName;

public class MsCatalogPage {

    public static final String addItemXpath = "//a[contains(text(),'Add Item')]";

    public MsCatalogPage addCatalogItem(String catalogItemName, String catalogItemPrice) {
        printMethodName();

        MsCommon.waitForElementClick(addItemXpath,"//h1[contains(text(),'Item : Edit')]");
        $(By.id("name")).shouldBe(Condition.visible).val(catalogItemName);
        $(By.id("price")).shouldBe(Condition.visible).val(catalogItemPrice);
        $(By.xpath("//button[contains(text(),'Submit')]")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);

        System.out.println("Successfully added catalog item: "+catalogItemName);

        return page(MsCatalogPage.class);
    }

    public MsMainPage navigateBackToMainPage() {
        printMethodName();

        MsCommon.navigateBackToMsMainPage();
        SeleniumHelper.myDontHurryTooMuch();

        return page(MsMainPage.class);
    }
}
