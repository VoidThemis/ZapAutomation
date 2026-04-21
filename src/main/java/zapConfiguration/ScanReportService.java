package zapConfiguration;

import browserFactory.BrowserFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.zaproxy.clientapi.core.ClientApiException;
import tools.Logger;
import tools.Waiting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;

public class ScanReportService {

    private static WebDriver driver;

    ZapApiClient zapClient = new ZapApiClient();
    Proxy proxy = zapClient.apiZapSetup();

    SpiderScan spiderScan = new SpiderScan(zapClient);

    public void startScanningApplication(String url, String id, String appName) throws Exception {
        BrowserFactory browserConfig = new BrowserFactory();
        Waiting.time(5000);
        driver = (WebDriver) browserConfig.BrowserSetupOptionsDriver(proxy,true,true);
        driver.get(url);
        Waiting.time(9000);
        //driver.manage().addCookie(new Cookie("OptanonAlertBoxClosed", "true"));

        try {
            Logger.info(ScanReportService.class, "Trying to close the cookies popUp");
            WebDriverWait wait = new WebDriverWait(driver, Waiting.time(5000));
            WebElement accept = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(.,'Accept')]"))
            );
            accept.click();
            Logger.info(ScanReportService.class, "Cookies popUP closed");
            Waiting.time(9000);
        } catch (Exception ignored) {
            Logger.error(ScanReportService.class,"Error by clicking the cookie popUp", ignored);
        }
        spiderScan.spiderScanning(url);
        Waiting.time(5000);
        spiderScan.passiveScanning(url);
        Waiting.time(5000);
        spiderScan.activeScanning(url);
        Waiting.time(5000);
        spiderScan.generateTheReport(id, appName);
    }


}
