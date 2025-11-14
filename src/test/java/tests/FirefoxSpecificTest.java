package tests;

import io.qameta.allure.Step;
import org.testng.annotations.Test;
import pages.InventoryPage;

public class FirefoxSpecificTest extends BaseTest {

    @Test
    @Step("10. Запуск в Firefox — успешный логин")
    public void testLoginInFirefox() {
        login("standard_user", "secret_sauce");
        new InventoryPage(driver).checkTitle();
    }
}