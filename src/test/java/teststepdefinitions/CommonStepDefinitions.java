package teststepdefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber_dependency_injection.World;
import microservice.common.MsCommon;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;
import java.util.List;

import static microservice.helper.SeleniumHelper.printMethodName;

public class CommonStepDefinitions {

    private World world;
    private Scenario scenario;

    public CommonStepDefinitions(World world) {
        this.world = world;

    }
    static Logger log = Logger.getLogger(
            CommonStepDefinitions.class.getName());


    @Before()
    public void before(Scenario scenario) {
        printMethodName();

        this.scenario = scenario;

        log.info(MsCommon.printScenarioPhaseText(scenario,"START"));

    }

    @After()
    public void after(Scenario scenario) {
        printMethodName();

        this.scenario = scenario;

        log.info(MsCommon.printScenarioPhaseText(scenario, "END"));

    }
}



