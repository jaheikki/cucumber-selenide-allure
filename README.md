Usage:
---------
mvn clean install -Dcucumber.options="--tags @catalog";mvn site

mvn clean install -Dcucumber.options="--tags @order";mvn site

mvn clean install -Dcucumber.options="--tags @all" -Dselenide.browser=chrome;mvn site

mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome -Dremote=http://localhost:4444/wd/hub

mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome -Dremote=http://localhost:4444/wd/hub -Denv=remote

mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome -Dremote=http://localhost:4444/wd/hub -Denv=remote -Dselenide.browser-size=800x600

mvn clean install -Dbrowser=microservice.browser.ChromeAppleIphone5DriverProvider -Dcucumber.options="--tags @order"

mvn clean install -Dbrowser=microservice.browser.ChromeAppleIphone5RemoteDriverProvider -Dcucumber.options="--tags @order" -Denv=remote

Note: before remote run see https://github.com/SeleniumHQ/docker-selenium (debub part) and must connect selenium debug container to NW where microservice resides e.g. docker network connect resources_default sleepy_minsky.
