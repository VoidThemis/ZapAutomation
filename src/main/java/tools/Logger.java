package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Logger {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /* =========================
       FILE LOGGER
       ========================= */

    private static BufferedWriter writer;

    static {
        try {

            File resultsDir = new File("results");

            if (!resultsDir.exists()) {
                resultsDir.mkdirs();
            }

            String filename = "scan_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
                    ".log";

            File logFile = new File(resultsDir, filename);

            writer = new BufferedWriter(new FileWriter(logFile, true));

        } catch (IOException e) {
            System.err.println("Failed to initialize file logger");
            e.printStackTrace();
        }
    }

    /* =========================
       ANSI COLORS
       ========================= */

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";

    /* =========================
       REQUEST ID (Thread Safe)
       ========================= */

    private static final ThreadLocal<String> requestId = new ThreadLocal<>();

    public static void startRequest() {
        requestId.set(UUID.randomUUID().toString().substring(0,8));
    }

    public static void setRequestId(String id) {
        requestId.set(id);
    }

    public static void clearRequest() {
        requestId.remove();
    }

    private static String getRequestId() {
        return requestId.get() != null ? requestId.get() : "N/A";
    }

    /* =========================
       CORE LOGGER
       ========================= */

    private static String buildLog(String level, String color, String source, String message) {

        String timestamp = LocalDateTime.now().format(formatter);
        String thread = Thread.currentThread().getName();
        String reqId = getRequestId();

        return String.format(
                "%s[%s] [%s] [req:%s] [Thread:%s] [%s] %s%s",
                color,
                timestamp,
                level,
                reqId,
                thread,
                source,
                message,
                RESET
        );
    }

    /* =========================
       FILE FORMAT (NO COLORS)
       ========================= */

    private static String buildFileLog(String level, String source, String message) {

        String timestamp = LocalDateTime.now().format(formatter);
        String thread = Thread.currentThread().getName();
        String reqId = getRequestId();

        return String.format(
                "[%s] [%s] [req:%s] [Thread:%s] [%s] %s",
                timestamp,
                level,
                reqId,
                thread,
                source,
                message
        );
    }

    private static void writeToFile(String log) {

        try {

            if (writer != null) {
                writer.write(log);
                writer.newLine();
                writer.flush();
            }

        } catch (IOException e) {
            System.err.println("Failed writing log file");
            e.printStackTrace();
        }
    }

    /* =========================
       LOG LEVELS
       ========================= */

    public static void info(Class<?> source, String message) {

        String console = buildLog("INFO - ", BLUE, source.getSimpleName(), message);
        String file = buildFileLog("INFO", source.getSimpleName(), message);

        System.out.println(console);
        writeToFile(file);
    }

    public static void status(Class<?> source, String message) {

        String console = buildLog("STATUS -- ", GREEN, source.getSimpleName(), message);
        String file = buildFileLog("STATUS", source.getSimpleName(), message);

        System.out.println(console);
        writeToFile(file);
    }

    public static void warning(Class<?> source, String message) {

        String console = buildLog("!WARNING! --", YELLOW, source.getSimpleName(), message);
        String file = buildFileLog("WARNING", source.getSimpleName(), message);

        System.out.println(console);
        writeToFile(file);
    }

    public static void debug(Class<?> source, String message) {

        String console = buildLog("DEBUG", CYAN, source.getSimpleName(), message);
        String file = buildFileLog("DEBUG", source.getSimpleName(), message);

        System.out.println(console);
        writeToFile(file);
    }

    public static void error(Class<?> source, String message, Exception e) {

        String console = buildLog("|| ERROR || --", RED, source.getSimpleName(), message);
        String file = buildFileLog("ERROR", source.getSimpleName(), message);

        System.err.println(console);
        writeToFile(file);

        if (e != null) {
            e.printStackTrace();
        }
    }

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public static void startTimer() {
        startTime.set(System.currentTimeMillis());
    }

    public static long executionTime() {
        Long start = startTime.get();

        if (start == null) {
            return -1;
        }

        return System.currentTimeMillis() - start;
    }

    public static void clearTimer() {
        startTime.remove();
    }
}