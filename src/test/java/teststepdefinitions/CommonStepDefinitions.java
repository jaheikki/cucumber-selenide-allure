package teststepdefinitions;

import cucumber_dependency_injection.World;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import microservice.common.MsCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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



