package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class InventoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    private final By title = By.cssSelector(".title");
    private final By burgerMenuBtn = By.id("react-burger-menu-btn");
    private final By aboutLink = By.id("about_sidebar_link");
    private final By shoppingCartLink = By.cssSelector(".shopping_cart_link");
    private final By sortContainer = By.cssSelector(".product_sort_container");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Проверка заголовка 'Products'")
    public InventoryPage checkTitle() {
        String actualTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText();
        Assert.assertEquals(actualTitle, "Products", "Заголовок должен быть 'Products'");
        return this;
    }

    @Step("Добавление товара в корзину: {itemName}")
    public InventoryPage addToCartByName(String itemName) {
        String xpath = String.format("//div[contains(text(), '%s')]/ancestor::div[@class='inventory_item']//button[contains(text(), 'Add to cart')]", itemName);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath))).click();
        return this;
    }

    @Step("Переход в корзину")
    public CartPage goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(shoppingCartLink)).click();
        return new CartPage(driver);
    }

    @Step("Сортировка по: {value}")
    public InventoryPage sortBy(String value) {
        driver.findElement(sortContainer).click();
        By option = By.cssSelector("option[value='" + value + "']");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
        return this;
    }

    @Step("Открытие бургер-меню")
    public InventoryPage openBurgerMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(burgerMenuBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(aboutLink));
        return this;
    }

    @Step("Получение ссылки 'About'")
    public String getAboutLinkHref() {
        return driver.findElement(aboutLink).getAttribute("href");
    }

    @Step("Проверка ссылки 'About' = https://saucelabs.com/")
    public InventoryPage verifyAboutLink() {
        String href = getAboutLinkHref();
        Assert.assertEquals(href, "https://saucelabs.com/", "Ссылка About должна вести на saucelabs.com");
        return this;
    }
}