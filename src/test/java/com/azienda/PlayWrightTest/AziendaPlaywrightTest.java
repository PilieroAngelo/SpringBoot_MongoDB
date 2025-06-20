package com.azienda.PlayWrightTest;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.annotations.*;

public class AziendaPlaywrightTest {

    private Playwright playwright;
    private Browser browser;
    private Page page;
    private final String baseUrl = "http://localhost:8080";

    @BeforeClass
    public void setup() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
    }

    @AfterClass
    public void teardown() {
        browser.close();
        playwright.close();
    }

    @Test
    public void testE2EFlow() {
        // STEP 1 - Home page
        page.navigate(baseUrl + "/");
        Assert.assertTrue(page.content().contains("Benvenuto nel sistema delle Aziende"));

        // STEP 2 - Creazione azienda
        page.navigate(baseUrl + "/aziende/nuovo");
        page.fill("#nomeAzienda", "TestAzienda");
        page.fill("#numeroPersonale", "50");
        page.fill("#PIVA", "ABCD1234EFGH");
        page.fill("#nettoAnnuale", "100000");
        page.click("button[type='submit']");
        page.waitForTimeout(2000);

        page.navigate(baseUrl + "/");
        Assert.assertTrue(page.content().contains("TestAzienda"));

        // STEP 3 - Modifica azienda
        page.click("text=Modifica");
        page.fill("#nomeAzienda", "AziendaModificata");
        page.click("button[type='submit']");
        page.waitForTimeout(2000);
        page.navigate(baseUrl + "/");
        Assert.assertTrue(page.content().contains("AziendaModificata"));

        // STEP 4 - Modifica parziale PATCH
        page.click("text=Modifica parziale");
        page.selectOption("#campoDaModificare", "numeroPersonale");
        page.fill("#nuovoValore", "75");
        page.click("button[type='submit']");
        page.waitForTimeout(2000);

        // STEP 5 - Eliminazione
        page.navigate(baseUrl + "/");
        page.onceDialog(dialog -> dialog.accept());
        page.click("text=Elimina");
        page.waitForTimeout(2000);
        page.navigate(baseUrl + "/");
        Assert.assertFalse(page.content().contains("AziendaModificata"));
    }
}
