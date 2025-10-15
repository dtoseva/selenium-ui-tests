package com.ui.tests;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ui.utils.ConfigEnv;

public class BaseTest {
    protected WebDriver driver;
    protected final String BASE_URL = "https://www.saucedemo.com/"; 
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    @BeforeEach
    public void setUp(){
        String env = System.getProperty("environment", "dev");
        ConfigEnv.loadEnv(env);

        String browser = System.getProperty("chrome", "firefox").toLowerCase();
        switch (browser) {
            case "firefox":
                System.setProperty("webdriver.gecko.driver",
                "C:\\Users\\d_tos\\Downloads\\geckodriver-v0.36.0-win32\\geckodriver.exe");
                FirefoxOptions option = new FirefoxOptions();
                option.addArguments("--width=1920");
                option.addArguments("--height=1080");
                driver = new FirefoxDriver(option);              
                break;
            case "chrome":
            default:
                System.setProperty("webdriver.chrome.driver",
            "C:\\Users\\d_tos\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");        
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-save-password-bubble");
                options.addArguments("disable-infobars");
                options.addArguments("--start-maximized");   
                driver = new ChromeDriver(options);                
                break;
        }          
                
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        
    }

    @AfterEach
    public void close(){
        if (driver != null) {
            driver.quit();
        }
    }

    protected void baseUrl(){
        driver.get(BASE_URL);
    }

    protected void login(String username, String password){
        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();
    }

    protected void logout() {        
        WebElement menu = driver.findElement(By.id("react-burger-menu-btn"));
        js.executeScript("arguments[0].click()", menu);
        WebElement logoutBtn = driver.findElement(By.id("logout_sidebar_link"));
        js.executeScript("arguments[0].click()", logoutBtn);
    }
 
    public void assertInput(By locator, String expected) {
        WebElement input = driver.findElement(locator);
        String actualInput = input.getAttribute("value");
        if(!actualInput.equals(expected)) {
            throw new RuntimeException("The input with locator: " + locator + 
            "is not filled, expected:" + expected + " but get: " + actualInput);
        }
    }
    
}