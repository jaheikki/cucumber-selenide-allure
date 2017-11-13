package microservice.pages;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import microservice.common.MsCommon;
import microservice.common.MsConstants;
import microservice.helper.SeleniumHelper;
import org.openqa.selenium.By;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static microservice.common.MsConstants.elementClickTimeoutMs;
import static microservice.helper.SeleniumHelper.printMethodName;


public class ProductsPage {

    public AddProductPage navigateToAddProductPage() {
        printMethodName();

        $(By.cssSelector("#addItem")).shouldBe(Condition.visible).click();
        $(By.xpath("//span[contains(text(),'Add Product')]")).shouldBe(Condition.visible);

        return page(AddProductPage.class);
    }

    public CustomersPage navigateToCustomersPage() {
        printMethodName();

        $(By.cssSelector("#tab-customers")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//span[contains(text(),'Customers')]")).shouldBe(Condition.visible);
        $(By.cssSelector("#addCustomer")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//span[contains(text(),'Add Customer')]")).shouldBe(Condition.visible);

        return page(CustomersPage.class);
    }

    public OrdersPage navigateToOrdersPage()  {
        printMethodName();

        //This works but not if 'Microservice' is not running yet:
        //$(By.xpath("//a[contains(text(),'Order')]")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        //$(By.xpath("//a[contains(text(),'Add Order')]")).shouldBe(Condition.visible);

        //Works in any case:
        int buttonPressTimeoutOld = MsConstants.elementClickTimeoutMs;
        elementClickTimeoutMs = 180000;
        MsConstants.doRefreshOnFailure = true;

        MsCommon.waitForElementClick("//a[contains(text(),'Order')]", "//a[contains(text(),'Add Order')]");

        elementClickTimeoutMs = buttonPressTimeoutOld;
        MsConstants.doRefreshOnFailure = false;
        SeleniumHelper.myDontHurryTooMuch();

        return page(OrdersPage.class);
    }
}
