package com.azienda.Controller;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AziendaControllerTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private String id;

    @BeforeAll
    void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(25));

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.get("http://localhost:8080/aziende");
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void navigateTo(String url) {
        driver.get("http://localhost:8080/aziende/" + url);
    }

    private WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private void clickButton(By locator) {
        findElement(locator).click();
    }

    @Test
    @Order(1)
    void testGetAllAziende() {
        navigateTo("index");
    }

    @Test
    @Order(2)
    void testCreateAzienda() {
        navigateTo("nuovo");

        WebElement nomeInput = findElement(By.id("nomeAzienda"));
        nomeInput.sendKeys("Test Azienda");

        WebElement numeroPersonaleInput = findElement(By.id("numeroPersonale"));
        numeroPersonaleInput.sendKeys("50");

        WebElement pivaInput = findElement(By.id("PIVA"));
        pivaInput.sendKeys("IT1234567890");

        WebElement nettoAnnualeInput = findElement(By.id("nettoAnnuale"));
        nettoAnnualeInput.sendKeys("100000");

        WebElement submitButton = findElement(By.xpath("//button[text()='Crea Azienda']"));
        submitButton.click();

        wait.until(ExpectedConditions.urlContains("/aziende"));
    }

    @Test
    @Order(3)
    void testUpdateAzienda() {
        // Wait for the list item to appear
        WebElement aziendalista = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("li")));

        // Use the correct XPath to locate the div that contains 'ID:' text
        WebElement aziendaID = aziendalista.findElement(By.xpath(".//div[contains(text(), 'ID:')]/span"));

        // Wait for the element to be visible and extract the ID
        wait.until(ExpectedConditions.visibilityOf(aziendaID));
        id = aziendaID.getText();

        // Navigate to modify the Azienda
        navigateTo("modifica/" + id);

        // Perform the update actions
        WebElement nomeAziendaField = findElement(By.id("nomeAzienda"));
        nomeAziendaField.clear();
        nomeAziendaField.sendKeys("Azienda Modificata Selenium");

        WebElement numeroPersonaleField = findElement(By.id("numeroPersonale"));
        numeroPersonaleField.clear();
        numeroPersonaleField.sendKeys("100");

        WebElement nettoAnnualeField = findElement(By.id("nettoAnnuale"));
        nettoAnnualeField.clear();
        nettoAnnualeField.sendKeys("200000");

        // Wait for the submit button to be clickable and visible
        WebElement submitButton = findElement(By.xpath("//button[text()='Aggiorna Azienda']"));
        submitButton.click();

        // Wait for redirection
        wait.until(ExpectedConditions.urlContains("/aziende"));
    }



    @Test
    void testDeleteAzienda() {
        navigateTo("elimina/" + id);

        WebElement deleteLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Elimina")));

        assertNotNull(deleteLink);

        deleteLink.click();

        Alert alert = driver.switchTo().alert();
        alert.accept();

        wait.until(ExpectedConditions.urlContains("/aziende"));
    }

    @Test
    void testCreateAziendaClientValidation() {
        navigateTo("nuovo");

        WebElement nomeInput = findElement(By.id("nomeAzienda"));
        nomeInput.clear();

        WebElement submitButton = findElement(By.xpath("//button[text()='Crea Azienda']"));
        submitButton.click();

        wait.until(ExpectedConditions.urlContains("/aziende/nuovo"));
    }

    @Test
    void testUpdateAziendaWithInvalidData() {
        navigateTo("modifica/"+ id);

        WebElement nomeAziendaField = findElement(By.id("nomeAzienda"));
        nomeAziendaField.clear();

        // Wait for the submit button to be clickable and visible
        WebElement submitButton = findElement(By.xpath("//button[text()='Aggiorna Azienda']"));
        submitButton.click();

        wait.until(ExpectedConditions.urlContains("/aziende/modifica"));

        WebElement errorMessage = findElement(By.id("nomeAzienda"));
        assertTrue(errorMessage.getAttribute("value").isEmpty());
    }
}
