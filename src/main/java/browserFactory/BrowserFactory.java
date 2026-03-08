package browserFactory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import tools.RandomNumersInRange;

import java.util.Objects;

/**
 * @author Dani "Bolombo" Bonilla
 */

public class BrowserFactory {

    public WebDriver driver;


    public Object BrowserSetupOptionsDriver(Proxy expectedProxy, Boolean acceptSSLCerts, Boolean acceptInsecCerts){
        driver = (WebDriver) browserSetup(Objects.requireNonNull(selectRandomBrowser()),expectedProxy,acceptSSLCerts,acceptInsecCerts);
        return driver;
    }


    private String selectRandomBrowser(){
       int browseMax = 2;
       int browserType = RandomNumersInRange.getRandomNumberInRange(1, browseMax);
       System.out.println("The browser selected is: ");
        switch (browserType) {
            case 1 -> {
                String chrome = "Chrome";
                System.out.println(chrome);
                return chrome;
            }
            case 2 -> {
                String firefox = "Firefox";
                System.out.println(firefox);
                return firefox;
            }
        }
       return null;
   }

    private Object browserSetup(String selectBrowser, Proxy expectedProxy, Boolean acceptSSLCerts, Boolean acceptInsecCerts){
        switch (selectBrowser) {
            case "Chrome" -> {
                driver = new ChromeDriver(chromeDriverSetup(expectedProxy, acceptSSLCerts, acceptInsecCerts));
                return driver;
            }
            case "Firefox" -> {
                driver = new FirefoxDriver(firefoxOptionsSetup(expectedProxy, acceptSSLCerts, acceptInsecCerts));
                return driver;
            }
        }
           return null;
       }

   //Difference's Browsers setup

    private ChromeOptions chromeDriverSetup(Proxy expectedProxy, Boolean acceptSSLCerts, Boolean acceptInsecCerts){
       WebDriverManager.chromedriver().setup();
       ChromeOptions chromeOpts = new ChromeOptions();
       chromeOpts.setCapability(CapabilityType.PROXY, expectedProxy);
       chromeOpts.setCapability(CapabilityType.ACCEPT_SSL_CERTS, acceptSSLCerts);
       chromeOpts.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, acceptInsecCerts);
       return chromeOpts;
   }
    private FirefoxOptions firefoxOptionsSetup(Proxy expectedProxy, Boolean acceptSSLCerts, Boolean acceptInsecCerts){
       WebDriverManager.firefoxdriver().setup();
       FirefoxOptions fireOpts = new FirefoxOptions();
       fireOpts.setCapability(CapabilityType.PROXY, expectedProxy);
       fireOpts.setCapability(CapabilityType.ACCEPT_SSL_CERTS, acceptSSLCerts);
       fireOpts.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, acceptInsecCerts);
       return fireOpts;
   }
}
