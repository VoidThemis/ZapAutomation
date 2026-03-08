package csvHandler;
import java.util.ArrayList;
import java.util.List;

public class CSVValidationResult {

    private final List<AppEntry> validEntries = new ArrayList<>();
    private final List<CSVValidationError> issues = new ArrayList<>();

    public List<AppEntry> getValidEntries() {
        return validEntries;
    }

    public List<CSVValidationError> getIssues() {
        return issues;
    }

    public void addValidEntry(AppEntry entry) {
        validEntries.add(entry);
    }

    public void addIssue(CSVValidationError issue) {
        issues.add(issue);
    }

    public boolean hasErrors() {
        return issues.stream().anyMatch(i -> i.getSeverity() == CSVValidationError.Severity.ERROR);
    }
}