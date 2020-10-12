package de.unibremen.swp;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunAsClient
@RunWith(Arquillian.class)
public class SeleniumIT {

    @Deployment
    public static WebArchive createDeployment() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        File war = Paths.get("target/uebung5-1.0.war").toFile();
        return ShrinkWrap.createFromZipFile(WebArchive.class, war);
    }

    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;

    @Before
    public void setUp() {
        driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void t1() {
        driver.get("http://localhost:8080/uebung5-1.0/login.xhtml/uebung5-1.0/users.xhtml");
        driver.manage().window().setSize(new Dimension(1036, 1118));
        driver.findElement(By.id("j_idt5:email")).click();
        driver.findElement(By.id("j_idt5:email")).sendKeys("a.amadou@uni-bremen.de");
        driver.findElement(By.id("j_idt5:password")).click();
        driver.findElement(By.id("j_idt5:password")).sendKeys("Amadou");
        driver.findElement(By.name("j_idt5:j_idt12")).click();
        driver.findElement(By.cssSelector("#form\\3Ausers\\3A 4\\3Aj_idt21 > .ui-cell-editor-output")).click();
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys("bernhard.berger@uni-bremen.de");
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys("${<KEY_ENTER>}");
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys("bernhard.berger@uni-bremen.de");
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys("${<KEY_ENTER>}");
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys(Keys.ESCAPE);
        driver.findElement(By.cssSelector("html")).click();
        {
            String value = driver.findElement(By.id("form:users:4:j_idt23")).getAttribute("value");
            assertThat(value, is("s_cychgf@uni-bremen.de"));
        }
        driver.findElement(By.cssSelector("#j_idt5\\3Aj_idt10 > .ui-button-text")).click();
    }
    @Test
    public void t2() {
        driver.get("http://localhost:8080/uebung5-1.0/login.xhtml");
        driver.manage().window().setSize(new Dimension(1036, 1118));
        driver.findElement(By.id("j_idt5:email")).click();
        driver.findElement(By.id("j_idt5:email")).sendKeys("a.amadou@uni-bremen.de");
        driver.findElement(By.id("j_idt5:password")).click();
        driver.findElement(By.id("j_idt5:password")).sendKeys("Amadou");
        driver.findElement(By.name("j_idt5:j_idt12")).click();
        driver.findElement(By.cssSelector("#form\\3Ausers\\3A 4\\3Aj_idt21 > .ui-cell-editor-output")).click();
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys("bernhard.berger@uni-bremen.de");
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector("#form\\3Ausers\\3A 3\\3Aj_idt21 > .ui-cell-editor-output")).click();
        {
            WebElement element = driver.findElement(By.id("form:users:4:j_idt23"));
            Boolean isEditable = element.isEnabled() && element.getAttribute("readonly") == null;
            assertTrue(isEditable);
        }
        driver.findElement(By.id("form:users:3:j_idt23")).click();
        driver.findElement(By.id("form:users:3:j_idt23")).click();
        driver.findElement(By.id("form:users:3:j_idt23")).click();
        driver.findElement(By.id("form:users:3:j_idt23")).click();
        driver.findElement(By.id("form:users:3:j_idt23")).sendKeys("hoelsch@uni-bremen.de");
        driver.findElement(By.id("form:users:3:j_idt23")).sendKeys(Keys.ENTER);
        driver.findElement(By.id("form:users:4:j_idt23")).click();
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys(Keys.ENTER);
        driver.findElement(By.id("form:users:3:j_idt23")).sendKeys("hoelsch@uni-bremen.de");
        driver.findElement(By.id("form:users:3:j_idt23")).click();
        driver.findElement(By.id("form:users:3:j_idt23")).sendKeys(Keys.ESCAPE);
        driver.findElement(By.id("form:users:4:j_idt23")).click();
        driver.findElement(By.id("form:users:4:j_idt23")).sendKeys(Keys.ESCAPE);
        {
            String value = driver.findElement(By.id("form:users:4:j_idt23")).getAttribute("value");
            assertThat(value, is("s_cychgf@uni-bremen.de"));
        }
        {
            String value = driver.findElement(By.id("form:users:3:j_idt23")).getAttribute("value");
            assertThat(value, is("bernhard.berger@unibremen.\\nde"));
        }
        driver.findElement(By.cssSelector("#j_idt5\\3Aj_idt10 > .ui-button-text")).click();
    }
}