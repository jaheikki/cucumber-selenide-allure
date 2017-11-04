package microservice.pages;


import com.codeborne.selenide.Condition;
import microservice.common.MsCommon;
import microservice.helper.SeleniumHelper;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static microservice.helper.SeleniumHelper.printMethodName;

public class MsOrderPage {

    public static final String addOrderXpath = "//a[contains(text(),'Add Order')]";

    public MsOrderPage deleteOrderByCustomer (String customer) {
        printMethodName();

        if ($(By.xpath("//td[contains(text(),'" + customer + "')]")).isDisplayed()) {
            MsCommon.waitForElementClick("//td[contains(text(),'"+customer+"')]/../..//input[contains(@class,' btn-link')]","");
            $(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);
            System.out.println("Order deleted for customer: "+ customer);
        } else {
            System.out.println("No orders for customer: "+ customer);
        }

        return page(MsOrderPage.class);
    }


    public MsOrderPage addOrderByCustomer(String catalogItem,String customer) {
        printMethodName();

        MsCommon.waitForElementClick("//a[contains(text(),'Add Order')]","//h1[contains(text(),'Order : Add')]");
        $(By.id("customerId")).shouldBe(Condition.visible).selectOption(customer);
        $(By.xpath("//button[contains(@name,'addLine')]")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.id("orderLine0.count")).shouldBe(Condition.visible).val("1");
        $(By.id("orderLine0.itemId")).shouldBe(Condition.visible).selectOption(catalogItem);
        $(By.xpath("//button[contains(@name,'submit')]")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);

        System.out.println("Order made successfully: "+ customer +","+catalogItem);

        return page(MsOrderPage.class);
    }

    public MsOrderPage verifyOrderByCustomer (String customer, String catalogItem, String price) {
        printMethodName();

        $(By.xpath("//table/tbody/tr[last()]/td[contains(text(),'"+customer+"')]/..//td[1]/a")).shouldBe(Condition.visible).click();
        $(By.xpath("//div[contains(text(),'"+customer+"')]")).shouldBe(Condition.visible);
        $(By.xpath("//div[contains(text(),'"+catalogItem+"')]")).shouldBe(Condition.visible);
        $(By.xpath("//div[contains(text(),'"+price+"')]")).shouldBe(Condition.visible);

        System.out.println("Correct order found: "+ customer +","+catalogItem + "," + price);

        return page(MsOrderPage.class);
    }

    public MsMainPage navigateBackToMainPage() {
        printMethodName();

        MsCommon.navigateBackToMsMainPage();
        SeleniumHelper.myDontHurryTooMuch();

        return page(MsMainPage.class);
    }
}
