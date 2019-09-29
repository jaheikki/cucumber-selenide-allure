# Cucumber & Selenide & Allure demo

## Latest news:

27.9.2019: Replaced log4j with logback + simple DataTable example in feature file (Scenario: Order a product from a catalog)

27.9.2019: Upgrade to Cucumber 4: https://cucumber.io/blog/announcing-cucumber-jvm-4-0-0
Note 1: this project does not use new Cucumber 4 features, but there's now an option to use them.
Note 2: use DataTable when you deliver e.g. list or map from feature file to step definition (conversion is then automatic).

13.1.2019: Taking REST-assured in use and thus replacing Jersey&Jackson use in Rest methods (Jersey&Jackson examples still exist in code)  

21.8.2018: Upgraded to Cucumber Java release 2 and TestNG (previously jUnit)!
Note: Could not upgrade yet to Cucumber Java release 3 since it has changes that are not backward compatible.

## Purpose of this project
To collect & present toolset for making E2E system tests written in Java by using common open source tools. This example project is kept very simple on purpose so that basically everyone is able to understand the code. With help of this "project template" you are able to develop more advanced tests, regarless if it is UI, REST, DB, SSH test or all of them.

#### Details
This is fully working example how run Web UI tests easily with Java with Selenide library (http://selenide.org/). In this example the demo application is set up by docker and docker-compose. The tests will be run by Cucumber Java framework to be able to use Gherkin (BDD) style as testcase descriptions (see https://docs.cucumber.io/bdd/overview and https://cucumber.io/blog/2017/08/29/announcing-cucumber-jvm-2-0-0 and ). And as cherry on top of a cake we use here Allure2 framework for producing world class test report (see https://github.com/allure-framework/allure2 and https://github.com/allure-framework/allure-java). There's also a small DB usage example in CustomersPage class method addCustomer, it uses Spring JDBC template to do some simple DB tasks.  Also with help of SSH libraries included you can operate on Linux servers in e2e automated system tests. See included pdf document for more information.

## Preconditions:
- Install docker for Mac: https://docs.docker.com/docker-for-mac/install/ 
- or install docker for Windows 10 Pro: https://docs.docker.com/docker-for-windows/install/
  - Note: only Windows 10 Pro with latest updates work. Use only PowerShell for docker commands.
- or install Vagrant box e.g. with Ubuntu 16.04 (https://app.vagrantup.com/ubuntu/boxes/xenial64) and install docker (https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/#install-docker-ce-1) and docker-compose (https://docs.docker.com/compose/install/) in it. See also: https://github.com/jaheikki/ansible
- Install JDK8, Maven3 and Git.
- Optional: Download free IntelliJ IDEA IDE Community Edition (https://www.jetbrains.com/idea) and install 'Cucumber For Java' plugin in IDE. This allows you to develop and debug Cucumber/Java tests easily (works fully in both Mac and Windows).
- To be able run testcases with BrowserStack you need to buy license or use trial period of BS. When you get your BS username and access key, set them up as environment variables in the shell you are running tests:
e.g export BROWSERSTACK_USERNAME=<username> && export BROWSERSTACK_ACCESS_KEY=<access_key>
Note: Maven VM option -Dlocal means testing the demo UI running (as docker containers) in your PC in BrowserStack, see: 
https://www.browserstack.com/automate/java

Notice that 'allure-cucumber4-jvm' Maven dependency brings the Cucumber core dependencies included, see pom.xml.

## Steps

1. Clone this repository (e.g. in ~/git dir): 
   - git clone https://github.com/jaheikki/cucumber-selenide-allure.git

2. Get dependended Git repositories cloned and build needed java applications:
   - cd ~/git/cucumber-selenide-allure
   - ./clone_and_build_ms_demo_apps.sh

4. Start demo application docker containers: 
   - cd ~/git/microservice-demo-acceptance-tests-copy/src/test/resources
   - docker-compose -f docker-compose-dev.yml up -d

5. Wait for demo application start a few minutes:
   - Web UI should appear in localhost:8080
   - Note: Use CMD-R(CTRL-R in Windows) + SHIFT to refresh page until demo application visible.

   <img src="https://raw.githubusercontent.com/jaheikki/cucumber-selenide-allure/master/readme_images/demo-application.png" width="400" height="500">
    
6. Run CucumberJVM tests:
    
     - Run 'catalog' tests (REST test without UI):
       - mvn clean install -Dcucumber.options="--tags @catalog"
     - Run 'order' tests by chrome browser:
       - mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome
     - Run 'order' tests by chrome browser with mobile size (Apple iPhone 6 size):  
       - mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=chrome -Dselenide.browser-size=375x667
     - Run 'order' tests by Firefox in standalone debug grid container (Selenide default Webriver capabilities):
       - mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome -Dremote=http://localhost:4445/wd/hub - Denv=local-grid (Note: take session to localhost:5900 with VNCViewer with pw 'secret')
       OR to run in grid with custom browser (customizable Webriver capabilities):
       - mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=microservice.browser.FirefoxLinuxGridDriverProvider -Denv=local-grid (Note: take session to localhost:5900 with VNCViewer with pw 'secret')
     - Run 'order' tests by custom firefox browser (from local page) in BrowserStack:   
       - mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=microservice.browser.Firefox53WindowsRemoteDriverProvider -Dlocal
     - Run 'order' tests by custom mobile browser (from local page)  in BrowserStack:   
       - mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=microservice.browser.SamsungGalaxyS7_V6_0_RemoteDriverProvider -            Dlocal
     - Run 'all' tests (catalog&order) and generate Allure test report files:
       - mvn clean install -Dcucumber.options="--tags @all" -Dselenide.browser=chrome
       - Generate the HTML report by: mvn allure:report
       - Open the test report ('Open in Browswer' in IntelliJ IDEA IDE project) from <project_dir>/target/site/allure-maven-plugin/index.html
       <img src="https://raw.githubusercontent.com/jaheikki/cucumber-selenide-allure/master/readme_images/allure-report.png" width="800" height="300">
  
7. Stop and remove application docker containers: 'docker-compose -f docker-compose-dev.yml down -v --remove-orphans' 
   Note: Sometimes demo application hangs and it is needed to be shut it down and restart (up -d, see step 3). Or use restart: 'docker-compose -f docker-compose-dev.yml restart'


