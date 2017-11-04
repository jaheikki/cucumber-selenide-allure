package microservice.pages;


import com.codeborne.selenide.Condition;
import microservice.common.MsCommon;
import microservice.helper.SeleniumHelper;
import org.openqa.selenium.By;

import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static microservice.helper.SeleniumHelper.printMethodName;

public class MsCustomerPage {

    public MsCustomerPage addCustomer(String customer) {
        printMethodName();

        HashMap<String,String> customerDataMap = null;

        if (customer.equals("Teemu Selanne")) {
            customerDataMap = getTeemuSelanneData();
        }

        MsCommon.waitForElementClick("//a[contains(text(),'Add Customer')]","//h1[contains(text(),'Customer : Edit')]");
        $(By.id("name")).shouldBe(Condition.visible).val(customerDataMap.get("name"));
        $(By.id("firstname")).shouldBe(Condition.visible).val(customerDataMap.get("firstname"));
        $(By.id("email")).shouldBe(Condition.visible).val(customerDataMap.get("email"));
        $(By.id("street")).shouldBe(Condition.visible).val(customerDataMap.get("street"));
        $(By.id("city")).shouldBe(Condition.visible).val(customerDataMap.get("city"));

        $(By.xpath("//button[contains(text(),'Submit')]")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);

        System.out.println("Successfully added customer: "+customer);

        return page(MsCustomerPage.class);
    }

    public MsOrderPage navigateBackToMainPage() {
        printMethodName();

        MsCommon.navigateBackToMsMainPage();
        SeleniumHelper.myDontHurryTooMuch();

        return page(MsOrderPage.class);
    }

    private HashMap<String, String> getTeemuSelanneData() {
        printMethodName();

        HashMap<String, String> map = new HashMap<String, String>();

        map.put("name", "Selanne");
        map.put("firstname", "Teemu");
        map.put("email", "Teemu@com");
        map.put("street", "Manhattan");
        map.put("city", "NY");

        return map;
    }
}
