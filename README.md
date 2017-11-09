#Cucumber & Selenide & Allure demo:
--------- 
Preconditions:
- Install docker for Mac: https://docs.docker.com/docker-for-mac/install/ 
- Install docker for Windows 10 Pro: https://docs.docker.com/docker-for-windows/install/

2. Clone this repository (e.g. in ~/git dir): git clone https://github.com/jaheikki/microservice-test-java-new.git 

3. Run 'clone_and_build_ms_demo_apps.sh' from this repo to clone dependended repositories and build needed java applications.

4. cd ~/git/microservice-demo-acceptance-tests-copy/src/test/resources

5. Start demo application docker containers: 'docker-compose -f docker-compose-dev.yml up -d'

6. Run CucumberJVM tests:
  Run 'catalog' tests by default browser (Firefox may not work properly):
  - mvn clean install -Dcucumber.options="--tags @catalog"

  Run 'order' tests by chrome browser:
  - mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome

  Run 'all' tests (catalog&order) and generate Allure test report:
  - mvn clean install -Dcucumber.options="--plugin io.qameta.allure.cucumberjvm.AllureCucumberJvm --tags @all" -Dselenide.browser=chrome
  
7. Stop and remove application docker containers: 'docker-compose -f docker-compose-dev.yml down -v --remove-orphans' 
   Note: Sometimes demo application hangs and it is needed to be shut it down and restart (up -d).


