package com.capge.pack;

import csvHandler.AppEntry;
import csvHandler.CSVReading;
import tools.Logger;
import zapConfiguration.SpiderScan;
import zapConfiguration.ZapApiClient;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Dani "Bolombo" Bonilla
 */

public class ZapAuto {


    /**
     * TODO:
     * 1. obtencion de datos del CSV ------------------ STATUS - DONE !!! :D
     * 2. Verificacion de los datos del CSV ----------- STATUS - DONE !!! :D
     * 3. Ejecucion del escaneo ------------------ STATUS - FAIL !!!!
     * 4. Generacion del informe ----------------- STATUS - FAIL !!!!
     */


    private final ZapApiClient zapClient;
    private final SpiderScan spiderScan;

    public ZapAuto() {
        this.zapClient = new ZapApiClient();
        this.spiderScan = new SpiderScan(zapClient);
    }

    public static void main(String[] args) {
        ZapAuto app = new ZapAuto();
        app.start();
    }

    private void start() {
        List<AppEntry> apps = loadAppsFromCsv("/home/apholo/red/logs/capgeminiApps/apps_ci_details.csv");
        Logger.info(ZapAuto.class, "Apps loaded: " + apps.size());

        Queue<AppEntry> queue = new LinkedList<>(apps);
        Logger.info(ZapAuto.class, "Queue size: " + queue.size());

        while (!queue.isEmpty()) {
            AppEntry appEntry = queue.poll();
            runScan(appEntry);
        }

        Logger.status(ZapAuto.class, "Execution Terminated -- Bye:D");
    }

    private void runScan(AppEntry app) {
        Logger.startRequest();
        Logger.startTimer();

        try {
            Logger.status(ZapAuto.class,"Start scanning app: " + app.getProjectID() + " - " + app.getProjectName());
            spiderScan.startScanningApplication(app.getPreproductionUrl(),app.getProjectID(),app.getProjectName());

        } catch (Exception e) {
            Logger.error(ZapAuto.class, "Failed during scan", e);
        } finally {
            Logger.clearTimer();
            Logger.clearRequest();
        }
    }

    private List<AppEntry> loadAppsFromCsv(String path) {
        Logger.startRequest();
        Logger.startTimer();

        try {
            Logger.status(ZapAuto.class, "Loading apps from CSV: " + path);
            return CSVReading.readAndValidate(path, true).getValidEntries();
        } catch (Exception e) {
            Logger.error(ZapAuto.class, "Failed to load CSV", e);
            return List.of();
        } finally {
            Logger.clearTimer();
            Logger.clearRequest();
        }
    }

    public void runZap() throws Exception {
        //runningZap();
    }

    public static final String ZAP_PROXYHOST = "localhost";
    public static final int ZAP_PROXYPORT = 8090;


}
