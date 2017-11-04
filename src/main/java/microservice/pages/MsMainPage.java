package microservice.pages;


import microservice.common.MsCommon;
import microservice.common.MsConstants;
import microservice.helper.SeleniumHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static microservice.common.MsConstants.elementClickTimeoutMs;
import static microservice.helper.SeleniumHelper.printMethodName;

public class MsMainPage {

    public MsCatalogPage navigateToCatalogPage() {
        printMethodName();

        int buttonPressTimeoutOld = MsConstants.elementClickTimeoutMs;
        elementClickTimeoutMs = 120000;
        MsConstants.doRefreshOnFailure = true;

        MsCommon.waitForElementClick("//div[contains(text(),'List / add / remove items')]/..//a[contains(text(),'Catalog')]",MsCatalogPage.addItemXpath);

        elementClickTimeoutMs = buttonPressTimeoutOld;
        MsConstants.doRefreshOnFailure = false;
        SeleniumHelper.myDontHurryTooMuch();

        return page(MsCatalogPage.class);
    }

    public MsCustomerPage navigateToCustomerPage() {
        printMethodName();

        int buttonPressTimeoutOld = MsConstants.elementClickTimeoutMs;
        elementClickTimeoutMs = 120000;
        MsConstants.doRefreshOnFailure = true;

        MsCommon.waitForElementClick("//a[contains(text(),'Customer')]","//h1[contains(text(),'Customer : View all')]");

        elementClickTimeoutMs = buttonPressTimeoutOld;
        MsConstants.doRefreshOnFailure = false;
        SeleniumHelper.myDontHurryTooMuch();

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
