package csvHandler;


public class CSVValidationError {

    public enum Severity {
        ERROR,
        WARNING
    }

    private final int lineNumber;
    private final Severity severity;
    private final String message;

    public CSVValidationError(int lineNumber, Severity severity, String message) {
        this.lineNumber = lineNumber;
        this.severity = severity;
        this.message = message;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return severity + " línea " + lineNumber + " -> " + message;
    }
}