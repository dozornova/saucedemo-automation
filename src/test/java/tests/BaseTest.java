package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    @Parameters("browser")
    @Step("Открываем браузер")
    public void setUp(@Optional("chrome") String browser) {

        if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else {
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();

            options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
            options.setExperimentalOption("useAutomationExtension", false);
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-blink-features=AutomationControlled");
            options.addArguments("--incognito");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            Map<String, Object> prefs = new HashMap<>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            prefs.put("profile.default_content_setting_values.notifications", 2);
            options.setExperimentalOption("prefs", prefs);

            driver = new ChromeDriver(options);

            ((JavascriptExecutor) driver).executeScript(
                    "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
            );
        }

        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @Step("Логин: {0}")
    public void login(String username, String password) {
        new pages.LoginPage(driver)
                .enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            // Убираем футер — чтобы не грузил Facebook/Twitter
            ((JavascriptExecutor) driver).executeScript(
                    "const footer = document.querySelector('.footer'); if (footer) footer.remove();"
            );

            // Делаем скриншот только при падении теста
            if (result.getStatus() == ITestResult.FAILURE) {
                saveScreenshot(result.getName());
            }

            driver.quit();
        }
    }

    // МЕТОД ДЛЯ СКРИНШОТОВ
    private void saveScreenshot(String testName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String path = "target/allure-results/" + testName + "_" + System.currentTimeMillis() + ".png";
            FileUtils.copyFile(screenshot, new File(path));

            // Добавляем скриншот в Allure
            Allure.addAttachment(testName + " (screenshot)", new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}