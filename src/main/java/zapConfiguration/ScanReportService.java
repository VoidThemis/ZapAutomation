package zapConfiguration;

import browserFactory.BrowserFactory;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
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

    protected void scanningURL(String url) throws Exception {
        BrowserFactory browserConfig = new BrowserFactory();
        Waiting.time(5000);
        driver = (WebDriver) browserConfig.BrowserSetupOptionsDriver(proxy,true,true);
        driver.get(url);
        Waiting.time(5000);
        spiderScan.spiderScanning(url);
        Waiting.time(5000);
        spiderScan.passiveScanning(url);
        Waiting.time(5000);
        spiderScan.activeScanning(url);
        Waiting.time(5000);
    }


}
