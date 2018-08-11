#!/usr/bin/env bash

source ~/bs_creds.sh

# init
function pause(){
   read -p "$*"
}

cd ~/git/cucumber-selenide-allure

echo "--------------------------------------------"
echo "Run 'Catalog' REST test:"
echo "--------------------------------------------"
pause 'Press [Enter] to run: mvn clean install -Dcucumber.options="--tags @catalog"'
mvn clean install -Dcucumber.options="--tags @catalog"
echo ""

echo "----------------------------------------------------------"
echo "Run 'Order' test in Demo UI & generate Allure test report:"
echo "----------------------------------------------------------"
pause 'Press [Enter] to run: mvn clean install -Dcucumber.options="--plugin io.qameta.allure.cucumberjvm.AllureCucumberJvm --tags @order" -Dbrowser=firefox'
mvn clean install -Dcucumber.options="--plugin io.qameta.allure.cucumberjvm.AllureCucumberJvm --tags @order" -Dbrowser=firefox
echo ""
pause 'Press [Enter] to run: mvn allure:report'
mvn allure:report
echo ""

echo "-------------------------------------------------------"
echo "Run 'Order' test in Demo UI with Apple iPhone 6 size:"
echo "-------------------------------------------------------"
pause 'Press [Enter] to run: mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=firefox -Dselenide.browser-size=375x667'
mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=firefox -Dselenide.browser-size=375x667
echo ""

echo "-----------------------------------------------------------------------"
echo "Run 'Order' test in Demo UI by CHROME browser in local Selenium grid container:"
echo "-----------------------------------------------------------------------"
pause 'Press [Enter] to run: mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome -Dremote=http://localhost:4445/wd/hub -Denv=local-grid'
mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=chrome -Dremote=http://localhost:4445/wd/hub -Denv=local-grid
echo ""

#echo "---------------------------------------------------------------------------------------"
#echo "Run 'Order' test in Demo UI by CUSTOM FIREFOX browser in local Selenium grid container:"
#echo "---------------------------------------------------------------------------------------"
#pause 'Press [Enter] to run: mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=microservice.browser.FirefoxLinuxGridDriverProvider -Denv=local-grid'
#mvn clean install -Dcucumber.options="--tags @order" -Dselenide.browser=microservice.browser.FirefoxLinuxGridDriverProvider -Denv=local-grid
#echo ""

echo "---------------------------------------------------------------------------------------"
echo "Run 'Order' test in LOCAL Demo UI by CUSTOM FIREFOX browser in BROWSERSTACK:"
echo "---------------------------------------------------------------------------------------"
pause 'Press [Enter] to run: mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=microservice.browser.Firefox53WindowsRemoteDriverProvider -Dlocal'
mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=microservice.browser.Firefox53WindowsRemoteDriverProvider -Dlocal
echo ""

echo "---------------------------------------------------------------------------------------"
echo "Run 'Order' test in LOCAL Demo UI by REAL MOBILE DEVICE browser in BROWSERSTACK:"
echo "---------------------------------------------------------------------------------------"
pause 'Press [Enter] to run: mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=microservice.browser.SamsungGalaxyS7_V6_0_RemoteDriverProvider -Dlocal'
mvn clean install -Dcucumber.options="--tags @order" -Dbrowser=microservice.browser.SamsungGalaxyS7_V6_0_RemoteDriverProvider -Dlocal
echo ""
