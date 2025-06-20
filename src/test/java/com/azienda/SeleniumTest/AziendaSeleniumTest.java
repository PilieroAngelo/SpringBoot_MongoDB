package com.azienda.SeleniumTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AziendaSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private final String baseUrl = "http://localhost:8080";

    @BeforeAll
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterAll
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testEndToEndFlow() throws InterruptedException {
        // STEP 1 - Carichiamo la home page
        driver.get(baseUrl + "/");
        Assertions.assertTrue(driver.getPageSource().contains("Benvenuto nel sistema delle Aziende"));

        // STEP 2 - Creazione di una nuova azienda (usiamo il form classico non ajax per semplicità)
        driver.get(baseUrl + "/aziende/nuovo");

        WebElement nomeAzienda = driver.findElement(By.id("nomeAzienda"));
        WebElement numeroPersonale = driver.findElement(By.id("numeroPersonale"));
        WebElement partitaIVA = driver.findElement(By.id("PIVA"));
        WebElement nettoAnnuale = driver.findElement(By.id("nettoAnnuale"));

        nomeAzienda.sendKeys("TestAzienda");
        numeroPersonale.sendKeys("50");
        partitaIVA.sendKeys("ABCD1234EFGH");
        nettoAnnuale.sendKeys("100000");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // Diamo un po' di tempo al redirect (se configurato)
        Thread.sleep(2000);

        // Torniamo alla home per verificare che l'azienda sia stata creata
        driver.get(baseUrl + "/");
        Assertions.assertTrue(driver.getPageSource().contains("TestAzienda"));

        // STEP 3 - Andiamo a modificare l'azienda (prendiamo il primo link "Modifica")
        WebElement modificaLink = driver.findElement(By.partialLinkText("Modifica"));
        modificaLink.click();

        WebElement nomeAziendaMod = driver.findElement(By.id("nomeAzienda"));
        nomeAziendaMod.clear();
        nomeAziendaMod.sendKeys("AziendaModificata");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Thread.sleep(2000);

        // Verifica che il nome sia stato modificato
        driver.get(baseUrl + "/");
        Assertions.assertTrue(driver.getPageSource().contains("AziendaModificata"));

        // STEP 4 - Facciamo una patch (modifica parziale)
        WebElement patchLink = driver.findElement(By.partialLinkText("Modifica parziale"));
        patchLink.click();

        WebElement campo = driver.findElement(By.id("campoDaModificare"));
        campo.sendKeys("numeroPersonale");
        WebElement nuovoValore = driver.findElement(By.id("nuovoValore"));
        nuovoValore.sendKeys("75");

        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // Qui ci sarebbe da attendere il redirect se configurato
        Thread.sleep(2000);

        // STEP 5 - Eliminiamo l'azienda
        driver.get(baseUrl + "/");
        WebElement eliminaLink = driver.findElement(By.partialLinkText("Elimina"));
        eliminaLink.click();

        // Conferma dell'alert Javascript
        driver.switchTo().alert().accept();

        Thread.sleep(2000);

        // Verifica che l'azienda non esista più
        driver.get(baseUrl + "/");
        Assertions.assertFalse(driver.getPageSource().contains("AziendaModificata"));
    }
}
