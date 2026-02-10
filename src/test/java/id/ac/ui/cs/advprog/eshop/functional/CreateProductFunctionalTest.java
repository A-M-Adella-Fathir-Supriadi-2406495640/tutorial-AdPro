package id.ac.ui.cs.advprog.eshop.functional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    @Test
    void userCanCreateProductAndSeeItInProductList() {
        // 1. User membuka halaman create product
        driver.get("http://localhost:" + port + "/product/create");

        // 2. User mengisi form
        WebElement nameInput = driver.findElement(By.name("productName"));
        WebElement quantityInput = driver.findElement(By.name("productQuantity"));

        nameInput.sendKeys("Sampo Cap Bambang");
        quantityInput.sendKeys("100");

        // 3. User submit form
        quantityInput.submit();

        // 4. User diarahkan ke halaman product list
        assertTrue(driver.getCurrentUrl().contains("/product/list"));

        // 5. User melihat produk yang baru dibuat di tabel
        String pageSource = driver.getPageSource();

        assertTrue(pageSource.contains("Sampo Cap Bambang"));
        assertTrue(pageSource.contains("100"));
    }
}

