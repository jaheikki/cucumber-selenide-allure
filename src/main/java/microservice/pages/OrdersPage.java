package microservice.pages;


import com.codeborne.selenide.Condition;
import microservice.common.MsCommon;
import microservice.helper.SeleniumHelper;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static microservice.helper.SeleniumHelper.printMethodName;

public class OrdersPage {

    private static final Logger log = LoggerFactory.getLogger(OrdersPage.class);

    public OrdersPage deleteOrderByCustomer (String customer) {
        printMethodName();

        if ($(By.xpath("//td[contains(text(),'" + customer + "')]")).isDisplayed()) {
            MsCommon.waitForElementClick("//td[contains(text(),'"+customer+"')]/../..//input[contains(@class,' btn-link')]","");
            $(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);
            log.info("Order deleted for customer: "+ customer);
        } else {
            log.info("No orders for customer: "+ customer);
        }

        return page(OrdersPage.class);
    }


    public OrdersPage addOrderByCustomer(String catalogItem, String customer) {
        printMethodName();

        $(By.cssSelector("#addOrder")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.id("customerId")).shouldBe(Condition.visible).selectOption(customer);
        $(By.xpath("//button[contains(@name,'addLine')]")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.id("orderLine0.count")).shouldBe(Condition.visible).val("1");
        $(By.id("orderLine0.itemId")).shouldBe(Condition.visible).selectOption(catalogItem);
        $(By.xpath("//button[contains(@name,'submit')]")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);

        log.info("Order made successfully: "+ customer +","+catalogItem);

        return page(OrdersPage.class);
    }

    public OrdersPage verifyOrderByCustomer (String customer, String catalogItem, String price) {
        printMethodName();

        $(By.xpath("//table/tbody/tr[last()]/td[contains(text(),'"+customer+"')]/..//td[1]/a")).shouldBe(Condition.visible).click();
        $(By.xpath("//div[contains(text(),'"+customer+"')]")).shouldBe(Condition.visible);
        $(By.xpath("//tr/td/span[contains(text(),'"+price+"')]/../..//td[contains(text(),'"+catalogItem+"')]")).shouldBe(Condition.visible);

        log.info("Correct order found: "+ customer +","+catalogItem + "," + price);

        return page(OrdersPage.class);
    }

    public ProductsPage navigateBackToProductsPage() {
        printMethodName();

        MsCommon.navigateBackToProductsPage();
        SeleniumHelper.myDontHurryTooMuch();

        return page(ProductsPage.class);
    }
}
