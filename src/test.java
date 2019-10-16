import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


public class test  {
    public static void main(String[] args) throws InterruptedException {

        //System.setProperty("webdriver.gecko.driver", "C:\\Users\\Martynas\\Documents\\geckodriver.exe");

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Martynas\\Documents\\chromedriver.exe");

        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface,
        // not the implementation.

        //FirefoxProfile p = new FirefoxProfile();
        //p.setPreference("javascript.enabled", true);

        WebDriver driver = new ChromeDriver();

        // And now use this to visit Google
        driver.get("https://classic.flysas.com/en/de/");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.id("ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_predictiveSearch_txtFrom"));

        element.click();

        // Enter something to search for
        element.sendKeys("ARN");

        Thread.sleep(2000);

        element.sendKeys(Keys.RETURN);

        //driver.findElement(By.id("ARN")).click();

        element = driver.findElement(By.id("ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_predictiveSearch_txtTo"));

        element.click();

        element.sendKeys("LHR");

        Thread.sleep(2000);

        element.sendKeys(Keys.RETURN);

        //driver.findElement(By.id("LHR")).click();

        // Now submit the form. WebDriver will find the form for us from the element
        //element.submit();

        Thread.sleep(2000);

        driver.findElement(By.id("ctl00_FullRegion_MainRegion_ContentRegion_ContentFullRegion_ContentLeftRegion_CEPGroup1_CEPActive_cepNDPRevBookingArea_Searchbtn_ButtonLink")).click();

        element = driver.findElement(By.id("WDSEffect_table_0"));

        element.getAttribute("innerHTML");

        System.out.println(element.toString());
        /*// Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());

        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("SAS");
            }
        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());*/

        //Close the browser
        //driver.quit();
    }
}