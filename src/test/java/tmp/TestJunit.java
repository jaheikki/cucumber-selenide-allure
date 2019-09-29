package tmp;

import io.qameta.allure.Link;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static junit.framework.TestCase.fail;

/**
 * Created by heikmjar on 29/10/2017.
 */

public class TestJunit {

    private static final Logger log = LoggerFactory.getLogger(TestJunit.class);


    @Link("https://example.org")
    @Link(name = "allure", type = "mylink")
    @Test
    public void testSomething() {
     log.info("testSomething");
    }

    @Issue("123")
    @Issue("432")
    @Test
    public void testSomething2() {
        log.info("testSomething2");
    }

    @TmsLink("test-1")
    @TmsLink("test-2")
    @Test
    public void testSomething3() {
        log.info("testSomething3");
        fail("No NullPointerException");
        //throw new RuntimeException("Gone bad...");
    }
}
