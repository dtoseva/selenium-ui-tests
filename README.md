# Selenium UI tests
This repository contains UI test scenarios for saucedemo.com using Selenium WebDriver with Java, JUnit and Allure report
## Technologies
- Selenium WebDriver
- Java
- Allure report
- Maven
- JUnit 5
## Setup
- Clone the repository 
git clone https://github.com/dtoseva/selenium-ui-tests.git
### Environments
- Dev
- Staging
- Test
  Properties files are located in src/test/resources
  How to run tests: mvn clean test -Denvironment=dev
#### Supported browsers
Chrome(defaut) and Firefox
How to run test: mvn clean test -Denvironment=dev -Dbrowser=firefox 
##### Allure Report
mvn allure:serve
##### Project structure
com.ui.tests -> test classes
com.ui.utils -> config environments file
