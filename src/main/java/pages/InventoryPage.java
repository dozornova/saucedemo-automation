package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class InventoryPage {
    private WebDriver driver;
    private By title = By.cssSelector(".title");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }
    public void checkTitle() {
        Assert.assertEquals(driver.findElement(title).getText(), "Products");
    }
    public void addToCartByName(String itemName) {
        String xpath = String.format("//div[contains(text(), '%s')]/ancestor::div[@class='inventory_item']//button[contains(text(), 'Add to cart')]", itemName);
        driver.findElement(By.xpath(xpath)).click();
    }
    public void goToCart() {
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
    }
    public void sortBy(String value) {
        driver.findElement(By.cssSelector(".product_sort_container")).click();
        driver.findElement(By.cssSelector("option[value='" + value + "']")).click();
    }
}