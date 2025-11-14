package com.qa;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;

import java.util.List;

import static org.testng.Assert.*;

public class LoginTest extends BaseTest {

    // Вспомогательный метод — логин
    private void login(String username, String password) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }

    @Test
    @Step("1. Успешный логин")
    public void testSuccessfulLogin() {
        login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.checkTitle();
    }

    @Test
    @Step("2. Логин с неверным паролем")
    public void testFailedLogin() {
        login("standard_user", "wrong");

        String error = driver.findElement(By.cssSelector("[data-test='error']")).getText();
        assertTrue(error.contains("do not match"), "Ошибка не содержит 'do not match'");
    }

    @Test
    @Step("3. Добавление товара в корзину")
    public void testAddToCart() {
        login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.checkItemInCart("Sauce Labs Backpack");
    }

    @Test
    @Step("4. Удаление товара из корзины")
    public void testRemoveFromCart() {
        login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.addToCartByName("Sauce Labs Backpack");
        inventoryPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.removeFirstItem();
        cartPage.checkCartEmpty();
    }

    @Test
    @Step("5. Сортировка по цене (low → high)")
    public void testSortLowToHigh() {
        login("standard_user", "secret_sauce");

        InventoryPage inventoryPage = new InventoryPage(driver);
        inventoryPage.sortBy("lohi");

        List<WebElement> prices = driver.findElements(By.cssSelector(".inventory_item_price"));
        List<Double> values = prices.stream()
                .map(p -> Double.parseDouble(p.getText().replace("$", "")))
                .toList();

        assertTrue(isSorted(values), "Товары не отсортированы по возрастанию цены");
    }

    private boolean isSorted(List<Double> list) {
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) < list.get(i - 1)) {
                return false;
            }
        }
        return true;
    }
}