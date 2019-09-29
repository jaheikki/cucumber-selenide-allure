package microservice.pages;


import com.codeborne.selenide.Condition;
import jdbc.MyDatasource;
import jdbc.SQLCommon;
import microservice.common.MsCommon;
import microservice.helper.SeleniumHelper;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.page;
import static microservice.common.MsVariables.mariaDBJDBCPassword;
import static microservice.common.MsVariables.mariaDBJDBCUrl;
import static microservice.common.MsVariables.mariaDBJDBUser;
import static microservice.helper.SeleniumHelper.printMethodName;

public class CustomersPage {

    private static final Logger log = LoggerFactory.getLogger(CustomersPage.class);

    private MyDatasource customerDatasource;
    private JdbcTemplate customerJDBCTemplate;

    public CustomersPage addCustomer(String firstname, String lastname) {
        printMethodName();

        MyDatasource customerDatasource=new MyDatasource(mariaDBJDBCUrl,mariaDBJDBUser,mariaDBJDBCPassword);

        customerDatasource.runSqlScript("customer.sql");

        this.customerJDBCTemplate = new JdbcTemplate(customerDatasource.getDataSource());
        String email = SQLCommon.sqlQueryForString(customerJDBCTemplate, "SELECT email FROM customerdb.customer WHERE lastname='"+lastname+"' AND firstname='"+firstname+"';");
        String street = SQLCommon.sqlQueryForString(customerJDBCTemplate, "SELECT street FROM customerdb.customer WHERE lastname='"+lastname+"' AND firstname='"+firstname+"';");
        String city = SQLCommon.sqlQueryForString(customerJDBCTemplate, "SELECT city FROM customerdb.customer WHERE lastname='"+lastname+"' AND firstname='"+firstname+"';");

        $(By.id("name")).shouldBe(Condition.visible).val(lastname);
        $(By.id("firstname")).shouldBe(Condition.visible).val(firstname);
        $(By.id("email")).shouldBe(Condition.visible).val(email);
        $(By.id("street")).shouldBe(Condition.visible).val(street);
        $(By.id("city")).shouldBe(Condition.visible).val(city);

        $(By.xpath("//button[contains(text(),'Save')]")).shouldBe(Condition.visible).shouldBe(Condition.enabled).click();
        $(By.xpath("//h1[contains(text(),'Success')]")).shouldBe(Condition.visible);

        log.info("Successfully added customer: "+firstname+" "+lastname);

        return page(CustomersPage.class);
    }

    public OrdersPage navigateBackToProductsPage() {
        printMethodName();

        MsCommon.navigateBackToProductsPage();
        SeleniumHelper.myDontHurryTooMuch();

        return page(OrdersPage.class);
    }

}
