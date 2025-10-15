package com.ui.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;


public class OrderItemsTest extends BaseTest{

    @Test
    @Epic("Shopping cart Test")
    @Feature("Validate main functional")
    @Story("Login, validate that items are visible and logout")
    @Tag("smoke")
    public void validateVisibleItems(){
        
        Allure.step("Open base URL", () -> baseUrl());
        Allure.step("Login with standart user",
        () -> login("standard_user", "secret_sauce"));
               

        Allure.step("Verify all items are visible", () -> {
            List<WebElement> allItems = driver.findElements(By.cssSelector(".inventory_item"));
        
            boolean allItemsVisible = true;
                for (WebElement item : allItems) {
                    if (!item.isDisplayed()){
                        allItemsVisible=false;

                    }
                }
            assertTrue(allItemsVisible, "The element are not visible");

        });

        Allure.step("Log out", () -> logout());
    }
    
    @Test
    @Epic("Shopping cart Test")
    @Feature("Add, remove items and checkout")
    @Story("Add and remove items from cart and finish the order")
    @Tag("regression")
    public void addItemsToCart() throws InterruptedException { 
        
        Allure.step("Open base URL", () -> baseUrl());
        Allure.step("Login with standart user",
        () -> login("standard_user", "secret_sauce"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
        Allure.step("Add the first and the last item in the cart, verify the correct items are added",
        () ->{
            List<WebElement> allItems = driver.findElements(By.cssSelector(".inventory_item"));
            WebElement firstItem = allItems.get(0);
            WebElement lastItem = allItems.get(allItems.size()-1);       
        
            String firstItemName = firstItem.findElement(By.cssSelector(".inventory_item_name")).getText();
            String lastItemName = lastItem.findElement(By.cssSelector(".inventory_item_name")).getText();        

            WebElement firstAddBtn = firstItem.findElement(By.cssSelector(".btn_inventory"));
            js.executeScript("arguments[0].scrollIntoView(true);", firstAddBtn);
            js.executeScript("arguments[0].click();", firstAddBtn);
            Thread.sleep(500);
   
            WebElement lastAddBtn = lastItem.findElement(By.cssSelector(".btn_inventory"));
            js.executeScript("arguments[0].scrollIntoView(true);", lastAddBtn);
            js.executeScript("arguments[0].click();", lastAddBtn);
            Thread.sleep(1000); 

            // Validate that the selected items are in the cart
            WebElement cartBtn = driver.findElement(By.className("shopping_cart_link"));        
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartBtn);      

            wait.until(ExpectedConditions.urlContains("cart.html"));

            List<WebElement> allCartItems = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
            (By.cssSelector(".cart_item")));

            boolean firstElement = allCartItems.stream()
                .anyMatch(e -> e.findElement(By.cssSelector(".inventory_item_name"))
                .getText()
                .equals(firstItemName));
        
            boolean lastElement = allCartItems.stream()
                .anyMatch(e -> e.findElement(By.cssSelector(".inventory_item_name"))
                .getText()
                .equals(lastItemName));
        
            assertTrue(firstElement, "First item has been added in the cart");
            assertTrue(lastElement, "Last item has been added in the cart");

            WebElement removeFirstItem = allCartItems.get(0);
            WebElement removeBtn = removeFirstItem.findElement(By.cssSelector(".cart_button"));       
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", removeBtn);

            WebElement continueBtn = driver.findElement(By.id("continue-shopping"));        
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", continueBtn);
            wait.until(ExpectedConditions.urlContains("inventory.html"));         
        
            List<WebElement> updatedItems = driver.findElements(By.cssSelector(".inventory_item"));
            WebElement beforeLastItem = updatedItems.get(allItems.size() - 2);
            String beforeLastItemName = beforeLastItem.findElement(By.cssSelector(".inventory_item_name"))
            .getText();

            // Add previous item
            WebElement beforeLastAddBtn = beforeLastItem.findElement(By.cssSelector(".btn_inventory"));              
            js.executeScript("arguments[0].click();", beforeLastAddBtn);
        
            WebElement cartBtnUpdate = driver.findElement(By.className("shopping_cart_link"));        
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cartBtnUpdate);

            wait.until(ExpectedConditions.urlContains("cart.html"));

            List<WebElement> allCartItemsUpdated = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy
            (By.cssSelector(".cart_item")));

            boolean beforeLastElement = allCartItemsUpdated.stream()
                .anyMatch(e -> e.findElement(By.cssSelector(".inventory_item_name"))
                .getText()
                .equals(beforeLastItemName));
        
            assertTrue(beforeLastElement, "Previous item has been added in the cart");
        });

        Allure.step("Checkout and finish the order", () -> {
            WebElement checkoutBtn = driver.findElement(By.id("checkout"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkoutBtn);

            wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));        
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));        

            WebElement firstName = driver.findElement(By.id("first-name"));       
            WebElement lastName = driver.findElement(By.id("last-name"));        
            WebElement postalCode = driver.findElement(By.id("postal-code"));  
        
            js.executeScript("arguments[0].value='Emma'; arguments[1].value='Smit'; arguments[2].value='1234';",
            firstName, lastName, postalCode);

            assertInput(By.id("first-name"), "Emma");
            assertInput(By.id("last-name"), "Smit");
            assertInput(By.id("postal-code"), "1234");

            //Due to dynamic elements, when you click the "Continue" button,
            //the form input is cleared.
            //To continue the test, add a breakpoint before clicking the "Continue" button
            //and fill in the input manually.
       
            WebElement continueOrderBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("continue")));
            js.executeScript("arguments[0].scrollIntoView(true);", continueOrderBtn);
            js.executeScript("arguments[0].click();", continueOrderBtn); 
       
            wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
        
            WebElement paymentValue = driver.findElement(By.cssSelector(".summary_value_label"));
            String paymentText = paymentValue.getText();
            WebElement shoppingInfoValue = driver.findElement(
                By.xpath("//div[contains(text(), 'Shipping Information')]/following-sibling::div[@class='summary_value_label']"));
            String shoppingText = shoppingInfoValue.getText();
            WebElement priceSubTotalValue = driver.findElement(By.cssSelector(".summary_subtotal_label"));
            String priceSubTotalText = priceSubTotalValue.getText();
            WebElement priceTaxValue = driver.findElement(By.cssSelector(".summary_tax_label"));
            String priceTaxText = priceTaxValue.getText();
            WebElement priceTotalValue = driver.findElement(By.cssSelector(".summary_total_label"));
            String priceTotalText = priceTotalValue.getText();

            assertTrue(paymentText.contains("SauceCard"), "Payment information is correct");
            assertTrue(shoppingText.contains("Free Pony Express Delivery!"), "Shopping Information is correct");
            assertTrue(priceSubTotalText.contains("Item total:"), "Item total is correct");
            assertTrue(priceTaxText.contains("Tax:"), "Tax is correct");
            assertTrue(priceTotalText.contains("Total:"), "Total is correct");

            WebElement finishBtn = driver.findElement(By.id("finish"));
            js.executeScript("arguments[0].click();", finishBtn);

            wait.until(ExpectedConditions.urlContains("checkout-complete.html"));

            WebElement emptyCartBtn = driver.findElement(By.cssSelector(".shopping_cart_link"));
            js.executeScript("arguments[0].click()", emptyCartBtn);

            wait.until(ExpectedConditions.urlContains("cart.html"));

            List<WebElement> emptyCart = driver.findElements(By.cssSelector(".cart_item"));
            assertTrue(emptyCart.isEmpty(), "The cart is empty");

        });

        Allure.step("Log out", () -> logout());

    }    
}
