package tests;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import java.util.List;

import static org.testng.Assert.*;

public class LoginTest extends BaseTest {

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

    @Test
    @Step("6. Оформление заказа — шаг 1")
    public void testCheckoutStep1() {
        login("standard_user", "secret_sauce");
        new InventoryPage(driver).addToCartByName("Sauce Labs Backpack");
        new InventoryPage(driver).goToCart();

        CartPage cartPage = new CartPage(driver);
        cartPage.clickCheckout(); // ДОЛЖЕН БЫТЬ!

        driver.findElement(By.id("first-name")).sendKeys("Sergey");
        driver.findElement(By.id("last-name")).sendKeys("Test");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();

        assertTrue(driver.getCurrentUrl().contains("checkout-step-two.html"));
    }

    @Test
    @Step("7. Burger menu → Logout")
    public void testLogout() {
        login("standard_user", "secret_sauce");

        driver.findElement(By.id("react-burger-menu-btn")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))).click();

        // ЖДЁМ ПЕРЕХОДА НА ГЛАВНУЮ
        wait.until(ExpectedConditions.urlToBe("https://www.saucedemo.com/"));

        assertTrue(driver.getCurrentUrl().equals("https://www.saucedemo.com/"));
    }

    @Test
    @Step("8-9. Footer + About — проверка ссылок (без переходов)")
    public void testFooterAndAboutLinks() {
        login("standard_user", "secret_sauce");

        // Footer — Twitter
        assertEquals(driver.findElement(By.cssSelector(".social_twitter a")).getAttribute("href"), "https://twitter.com/saucelabs");

        // Footer — Facebook
        assertEquals(driver.findElement(By.cssSelector(".social_facebook a")).getAttribute("href"), "https://www.facebook.com/saucelabs");

        // Footer — LinkedIn
        assertEquals(driver.findElement(By.cssSelector(".social_linkedin a")).getAttribute("href"), "https://www.linkedin.com/company/sauce-labs/");

        // About — в бургер-меню (открываем меню)
        driver.findElement(By.id("react-burger-menu-btn")).click();
        String aboutHref = driver.findElement(By.id("about_sidebar_link")).getAttribute("href");
        assertEquals(aboutHref, "https://saucelabs.com/");
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