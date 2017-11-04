package cucumbertest.runner;

import io.qameta.allure.Link;
import io.qameta.allure.Issue;
import io.qameta.allure.TmsLink;
import org.junit.Test;

import static junit.framework.TestCase.fail;

/**
 * Created by heikmjar on 29/10/2017.
 */

public class testJunit {


    @Link("https://example.org")
    @Link(name = "allure", type = "mylink")
    @Test
    public void testSomething() {
     System.out.println("testSomething");
    }

    @Issue("123")
    @Issue("432")
    @Test
    public void testSomething2() {
        System.out.println("testSomething2");
    }

    @TmsLink("test-1")
    @TmsLink("test-2")
    @Test
    public void testSomething3() {
        System.out.println("testSomething3");
        fail("No NullPointerException");
        //throw new RuntimeException("Gone bad...");
    }
}
