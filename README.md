# Cucumber & Selenide & Allure demo

This is fully working example how run Web UI tests easily with Java with Selenide library (http://selenide.org/). In this example the demo application is set up by docker and docker-compose. The tests will be run by CucumberJVM framework to be able Gherkin (BDD) style testcase descriptions (https://cucumber.io/docs/reference/jvm). And as cherry on top of a cake we use here Allure2 framework for producing world class test report (https://github.com/allure-framework/allure2).

Preconditions:
- Install docker for Mac: https://docs.docker.com/docker-for-mac/install/ 
- or install docker for Windows 10 Pro: https://docs.docker.com/docker-for-windows/install/
  - Note: only Windows 10 Pro with latest updates work. Use only PowerShell for docker commands.
- or install Vagrant box e.g. with Ubuntu 16.04 and install docker and docker compose in it.
- Install JDK8, Maven3 and Git.


1. Clone this repository (e.g. in ~/git dir): 
   - git clone https://github.com/jaheikki/microservice-test-java-new.git 

2. Get dependended Git repositories cloned and build needed java applications:
   - cd ~/git/microservice-test-java-new
   - chmod 755 clone_and_build_ms_demo_apps.sh && ./clone_and_build_ms_demo_apps.sh

4. Start demo application docker containers: 
   - cd ~/git/microservice-demo-acceptance-tests-copy/src/test/resources
   - docker-compose -f docker-compose-dev.yml up -d

5. Wait for demo application start a few minutes:
   - Web UI should appear in localhost:8080
   - Note: Use CMD-R(CTRL-R) + SHIFT to refresh page until demo application visible.
   
6. Run CucumberJVM tests:
    
  - Run 'catalog' tests by default browser (Firefox may not work properly):
     * mvn clean install -Dcucumber.options="--tags @catalog"

  - Run 'order' tests by chrome browser:
    * mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome

  - Run 'all' tests (catalog&order) and generate Allure test report:
    * mvn clean install -Dcucumber.options="--plugin io.qameta.allure.cucumberjvm.AllureCucumberJvm --tags @all" -Dselenide.browser=chrome
  
7. Stop and remove application docker containers: 'docker-compose -f docker-compose-dev.yml down -v --remove-orphans' 
   Note: Sometimes demo application hangs and it is needed to be shut it down and restart (up -d).


