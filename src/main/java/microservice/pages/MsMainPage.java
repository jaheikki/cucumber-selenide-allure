package microservice.pages;


import com.codeborne.selenide.Condition;
import microservice.common.MsCommon;
import microservice.common.MsConstants;
import microservice.helper.SeleniumHelper;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static microservice.common.MsConstants.elementClickTimeoutMs;
import static microservice.helper.SeleniumHelper.printMethodName;

public class MsMainPage {

    public MsCatalogPage navigateToCatalogPage() {
        printMethodName();

        $(By.cssSelector("#addItem")).shouldBe(Condition.visible).click();
        $(By.xpath("//span[contains(text(),'Add Product')]")).shouldBe(Condition.visible);

        return page(MsCatalogPage.class);
    }

    public MsCustomerPage navigateToCustomerPage() {
        printMethodName();

        $(By.cssSelector("#tab-customers")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//span[contains(text(),'Customers')]")).shouldBe(Condition.visible);
        $(By.cssSelector("#addCustomer")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//span[contains(text(),'Add Customer')]")).shouldBe(Condition.visible);

        return page(MsCustomerPage.class);
    }

    public MsOrderPage navigateToOrderPage() {
        printMethodName();

        int buttonPressTimeoutOld = MsConstants.elementClickTimeoutMs;
        elementClickTimeoutMs = 120000;
        MsConstants.doRefreshOnFailure = true;

        MsCommon.waitForElementClick("//a[contains(text(),'Order')]",MsOrderPage.addOrderXpath);

        elementClickTimeoutMs = buttonPressTimeoutOld;
        MsConstants.doRefreshOnFailure = false;
        SeleniumHelper.myDontHurryTooMuch();

        return page(MsOrderPage.class);
    }
}
