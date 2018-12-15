import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import org.apache.commons.io.FileUtils;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.Duration;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

public class Task2 {
    public static int PageNumber=0;
    private static final ChromeDriver driver=new ChromeDriver();
    public static final FluentWait wait=new FluentWait(driver);
    static {
        final int price;
        Random random=new Random(1);
        Scanner in=new Scanner(System.in);
        wait.ignoring(TimeoutException.class);
        wait.withTimeout(Duration.ofMillis(20000));
        String exe="/usr/local/bin/chromedriver";
        System.setProperty("webdriver.chrome.driver",exe);
        ChromeOptions option=new ChromeOptions();
        option.addArguments("--start-maximized","--start-fullscreen");
        driver.manage().window().maximize();
        driver.navigate().to("https://rozetka.com.ua/notebooks/c80004/filter/");
        price=random.nextInt(220642-29)+29;
        WebElement Lowerlimit=driver.findElement(By.xpath("//*[@id='price[min]']"));
        Lowerlimit.clear();
        Lowerlimit.sendKeys(Integer.toString(price));
        driver.findElement(By.xpath("//*[@id='submitprice']")).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        wait.ignoring(StaleElementReferenceException.class);
        wait.until(o->{return TestPriceList(price);});
        driver.quit();
    }
    public static boolean TestPriceList(int price){
        for(WebElement element : driver.findElements(By.xpath("//*[@id='catalog_goods_block']/div/descendant::div[@class='g-price-uah']")))
        {
            wait.until(o->{return element.isDisplayed();});
            String link=element.getText().replace("грн","").replace(" ","");
            System.out.println(link+" uah");
            if(Integer.parseInt(link.substring(0,link.length()-1))<price)
            return false;
        }
        return true;
    }
}
