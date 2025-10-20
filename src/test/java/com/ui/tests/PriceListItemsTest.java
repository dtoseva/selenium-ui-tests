package com.ui.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import io.qameta.allure.Allure;


public class PriceListItemsTest extends BaseTest {
    @Test
    @Tag("smoke")
    public  void sortItemsByPrice() {
        
        Allure.step("Open base URL", () -> baseUrl());
        Allure.step("Login with standart user",
        () -> login("standard_user", "secret_sauce"));       

        Allure.step("Verify that Price (high to low) is selected", () -> {
            WebElement filter = driver.findElement(By.cssSelector(".product_sort_container"));            
            Select selectOption = new Select(filter);
            String currentOption = selectOption.getFirstSelectedOption().getAttribute("value");
            assertNotEquals("hilo", currentOption,
         "Expected filter Price (high to low) is not selected");
            selectOption.selectByValue("hilo");                  

            List<WebElement> items = driver.findElements(By.cssSelector(".inventory_item_price"));
            List<Double> prices = items.stream()
                .map(item -> Double.parseDouble(item.getText().replace("$","")))
                .collect(Collectors.toList());

            List<Double> sortPrices = new ArrayList<>(prices);
            sortPrices.sort(Collections.reverseOrder());

            assertEquals(sortPrices, prices, "The items are not sorted by high to low"); 

        });

        Allure.step("Log out", () -> logout());       

    }

}
