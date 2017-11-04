Usage:
---------
mvn clean install -Dcucumber.options="--tags @catalog";mvn allure:report

mvn clean install -Dcucumber.options="--tags @all" -Dselenide.browser=chrome;mvn allure:report

