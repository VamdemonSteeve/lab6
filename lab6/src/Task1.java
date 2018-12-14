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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Task1 {
    public static int PageNumber=0;
    private static  WebDriver phantom;
    private static final ChromeDriver driver=new ChromeDriver();
    public static final FluentWait wait=new FluentWait(driver);
    static{
        wait.ignoring(TimeoutException.class);
        String exe="/usr/local/bin/chromedriver";
        System.setProperty("webdriver.chrome.driver",exe);
        ChromeOptions option=new ChromeOptions();
        option.addArguments("--start-maximized","--start-fullscreen");
    }
    public static class Test1 implements Runnable{
        @Override public void run()
        {

        }
    }
    public static void ChangeTimeLimit(long  duration)
    {
        wait.withTimeout(Duration.ofMillis(duration));
    }
    public static void Back_up(){
        while(PageNumber>0){
            driver.navigate().back();
            PageNumber--;
        }
    }
    public static void InitBrowser(){
        wait.until(o -> {
            driver.navigate().to("https://www.google.com/");
            return true;
        });
    }
    public static void InputGoogle(String s) throws InterruptedException {
        WebElement Search=driver.findElement(By.xpath("//*[@id='tsf']/div[2]/div/div[1]/div/div[1]/input"));
        Search.clear();
        Search.click();
        if(Search.isEnabled())
            Search.sendKeys(s);
        Thread.sleep(1000);
        Search.submit();
    }
    public static boolean SearchGoogle(String s,boolean ScreenEverything)
    {

        while(true){
            if(ScreenEverything) {
                phantom.get(driver.getCurrentUrl());
                File scrFile = ((TakesScreenshot) phantom).getScreenshotAs(OutputType.FILE);
                try {
                    FileUtils.copyFile(scrFile, new File("Screens/NotFound/screen" + "PageNumber"+PageNumber));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PageNumber++;
            for(WebElement element : driver.findElements(By.xpath("//div[@class='r']/a")))
            {
                    if(element.getAttribute("href").equals(s)||ScreenEverything) {
                        if (element.getAttribute("href").equals(s)) {
                            System.out.println("search result: " + element.getAttribute("href"));
                            System.out.println("page's number: " + PageNumber);
                            File scrFile = ((TakesScreenshot) phantom).getScreenshotAs(OutputType.FILE);
                            try {
                                if (!ScreenEverything)
                                    FileUtils.copyFile(scrFile, new File("Screens/screen" + "PageNumber"));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    }
        }
            if(PageNumber==22)
                return false;
            driver.manage().window().setPosition(new Point(PageNumber,PageNumber));
            driver.findElement(By.xpath("//*[@id='pnnext']")).click();
        }
    }
    public static void main(String[] arg) throws Throwable
    {
        try {
            ChangeTimeLimit(25000);
            InitBrowser();
            DesiredCapabilities capabilities=new DesiredCapabilities();
            capabilities.setJavascriptEnabled(true);
            capabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"/usr/local/bin/phantomjs");
            phantom=new PhantomJSDriver(capabilities);
            driver.manage().window().maximize();
            InputGoogle("dojo");;
            wait.until(o->{return SearchGoogle("https://wynncraft.gamepedia.com/Enter_the_Dojo",false);});
            System.out.println("--------------");
            Back_up();
            InputGoogle("dojo");
            wait.until(o->{return SearchGoogle("https://dojotoolkit.org/",false);});
            System.out.println("--------------");
            Back_up();
            InputGoogle("dojo");
            SearchGoogle("cat.dog.com",true);
        }
        finally{
            driver.quit();
        }

    }
}
