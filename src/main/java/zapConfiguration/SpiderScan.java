package zapConfiguration;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import tools.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;


public class SpiderScan {

    private final ClientApi zapApi;


    public SpiderScan(ZapApiClient zapClient) {
        this.zapApi = zapClient.getZapApp();
    }

    public void spiderScanning(String url) throws ClientApiException {
        Logger.startRequest();
        Logger.startTimer();
        try {
            Logger.status(SpiderScan.class, "-- Starting the SPIDER Scan --");
            zapApi.spider.scan(url,null, null,null,null);
            Logger.status(SpiderScan.class, "-- The SPIDER Scan was Complete!! --");
            Logger.status(SpiderScan.class,
                    "Execution time: " + Logger.executionTime() + " ms");
        } catch (Exception e) {
            Logger.error(SpiderScan.class, "-- The Spider Scan has suffer a error during the process: -> ", e);
        }finally {
            Logger.clearTimer();
            Logger.clearRequest();
        }
    }

    public void passiveScanning(String url) throws Exception {
        Logger.startRequest();
        Logger.startTimer();
        Logger.status(SpiderScan.class, "-- Starting the SPIDER Scan --");
        try {
            zapApi.pscan.enableAllScanners(); // enable passive scanner.
            ApiResponse response = zapApi.pscan.recordsToScan(); // getting a response

            //iterating till we get response as "0".
            while(!response.toString().equals("100")) {
                response =	zapApi.pscan.recordsToScan();
            }
        } catch (ClientApiException e1) {
            Logger.error(SpiderScan.class, "-- The Passive Scanning has suffer a error during the process: ->", e1);
        }finally {
            Logger.status(SpiderScan.class, "Passive scan completed! ---");
            Logger.info(SpiderScan.class, "-- Waiting for scan progress to complete --");
            Logger.clearTimer();
            Logger.clearRequest();
        }
    }

    public void activeScanning(String url) throws Exception {
        Logger.startRequest();
        Logger.startTimer();
        try {
            zapApi.ascan.scan(url,"true","false", null, null, null);
            zapApi.activeScanSiteInScope(url);
            System.out.println("--- Scan Progress completed! ---");
        } catch (Exception e) {
            Logger.error(SpiderScan.class, "-- The Active Scanning has suffer a error during the process: ->", e);
        }finally {
            Logger.clearTimer();
            Logger.clearRequest();
        }

    }

    public void removeAndCleanTheSession() throws ClientApiException {
        Logger.startRequest();
        try {
            Logger.info(SpiderScan.class, "-- Cleaning the sessions");
            zapApi.ascan.removeAllScans();
            zapApi.core.newSession("","");
            Logger.info(SpiderScan.class, "-- Sessions killed --");
        } catch (Exception e) {
            Logger.error(SpiderScan.class, "-- We cant clear the sessions", e);
        }finally {
            Logger.clearTimer();
            Logger.clearRequest();
        }
    }

    public void generateTheReport(String id, String appName) {
        Logger.startRequest();
        Logger.startTimer();
        String reportName = id + "-"+ appName;
        try {
            Logger.info(ScanReportService.class, "-- Generating the scanning report");
            String report = new String(zapApi.core.htmlreport());
            Path fileReportPath = Paths.get(System.getProperty("user.dir") + "/scanZAPAuto/"+ DateFormat.getDateInstance() + reportName + ".html");
            Files.deleteIfExists(fileReportPath);
            Files.write(fileReportPath, report.getBytes());
            removeAndCleanTheSession();
        } catch (Exception e) {
            Logger.error(SpiderScan.class, "-- We cant generate the report. ERROR -> ", e);
        }finally {
            Logger.clearTimer();
            Logger.clearRequest();
        }

    }

    public void startScanningApplication(String url, String id, String appName) throws Exception {
        spiderScanning(url);
        passiveScanning(url);
        activeScanning(url);
        generateTheReport(id, appName);
    }

}
