package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class CartPage {
    private WebDriver driver;
    private By checkoutButton = By.id("checkout");
    private By continueShopping = By.id("continue-shopping");
    private By removeButton = By.cssSelector("[data-test^='remove']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickCheckout() {
        driver.findElement(By.id("checkout")).click();
    }

    public void clickContinueShopping() {
        driver.findElement(continueShopping).click();
    }

    public void removeFirstItem() {
        driver.findElement(removeButton).click();
    }

    public void checkItemInCart(String itemName) {
        Assert.assertTrue(driver.getPageSource().contains(itemName), "Товар не в корзине: " + itemName);
    }

    public void checkCartEmpty() {
        Assert.assertTrue(driver.findElements(By.cssSelector(".cart_item")).isEmpty(), "Корзина не пуста");
    }
}