package teststepdefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import cucumber_dependency_injection.World;
import jdbc.SQLCommon;
import microservice.common.MsCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(CommonStepDefinitions.class);


    @Before()
    public void before(Scenario scenario) {
        log.info(printMethodName());

        this.scenario = scenario;

        log.info(MsCommon.printScenarioPhaseText(scenario,"START"));

    }

    @After()
    public void after(Scenario scenario) {
        log.info(printMethodName());

        this.scenario = scenario;

        log.info(MsCommon.printScenarioPhaseText(scenario, "END"));

    }
}



