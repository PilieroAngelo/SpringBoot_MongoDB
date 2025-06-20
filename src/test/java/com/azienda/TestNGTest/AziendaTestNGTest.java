package com.azienda.TestNGTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

public class AziendaTestNGTest {

    private WebDriver driver;
    private final String baseUrl = "http://localhost:8080";

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterClass
    public void teardown() {
        driver.quit();
    }

    @Test
    public void testFlow() throws InterruptedException {
        // Home
        driver.get(baseUrl + "/");
        Assert.assertTrue(driver.getPageSource().contains("Benvenuto nel sistema delle Aziende"));

        // Inserimento azienda
        driver.get(baseUrl + "/aziende/nuovo");
        driver.findElement(By.id("nomeAzienda")).sendKeys("TestAzienda");
        driver.findElement(By.id("numeroPersonale")).sendKeys("50");
        driver.findElement(By.id("PIVA")).sendKeys("ABCD1234EFGH");
        driver.findElement(By.id("nettoAnnuale")).sendKeys("100000");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Thread.sleep(2000);
        driver.get(baseUrl + "/");
        Assert.assertTrue(driver.getPageSource().contains("TestAzienda"));

        // Modifica azienda
        driver.findElement(By.partialLinkText("Modifica")).click();
        WebElement nomeAziendaMod = driver.findElement(By.id("nomeAzienda"));
        nomeAziendaMod.clear();
        nomeAziendaMod.sendKeys("AziendaModificata");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Thread.sleep(2000);
        driver.get(baseUrl + "/");
        Assert.assertTrue(driver.getPageSource().contains("AziendaModificata"));

        // Patch azienda
        driver.findElement(By.partialLinkText("Modifica parziale")).click();
        driver.findElement(By.id("campoDaModificare")).sendKeys("numeroPersonale");
        driver.findElement(By.id("nuovoValore")).sendKeys("75");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        Thread.sleep(2000);
        driver.get(baseUrl + "/");

        // Eliminazione
        driver.findElement(By.partialLinkText("Elimina")).click();
        driver.switchTo().alert().accept();

        Thread.sleep(2000);
        driver.get(baseUrl + "/");
        Assert.assertFalse(driver.getPageSource().contains("AziendaModificata"));
    }
}
