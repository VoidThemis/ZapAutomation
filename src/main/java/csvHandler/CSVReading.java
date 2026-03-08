package csvHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReading {

    public static CSVValidationResult readAndValidate(String filePath, boolean hasHeader) throws IOException {
        CSVValidationResult result = new CSVValidationResult();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1 && hasHeader) {
                    continue;
                }

                if (line.trim().isEmpty()) {
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.WARNING,
                            "Línea vacía ignorada"
                    ));
                    continue;
                }

                String[] values = line.split(",", -1);

                String appKey = normalize(values, 0);
                String identifier = normalize(values, 1);
                String loginType = normalize(values, 2);
                String headerName = normalize(values, 3);
                String headerToken = normalize(values, 4);
                String url = normalize(values, 5);
                String projectKey = normalize(values, 6);
                String projectID = normalize(values, 7);
                String projectName = normalize(values, 8);

                boolean valid = true;

                if (appKey == null) {
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.WARNING,
                            "WARNING: Campo 'AppKey' vacío"
                    ));
                    valid = false;
                }

                if (identifier == null) {
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.WARNING,
                            "WARNING: Campo 'identifier' vacío"
                    ));
                    valid = false;
                }
                
                if (loginType != null && headerName == null) {
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.WARNING,
                            "WARNING: No hay loginType definido"
                    ));
                }
                if (headerName != null && headerToken == null) {
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.ERROR,
                            "ERROR: Hay un HeaderName pero falta headerToken"
                    ));
                }

                if (headerToken != null && headerName == null) {
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.WARNING,
                            "WARNING: Hay headerToken pero falta headerName"
                    ));
                }

                if (url == null) {
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.ERROR,
                            "ERROR: Campo 'url' vacío Campo obligatorio"
                    ));
                    valid = false;
                } else if (!isValidUrl(url)) {
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.ERROR,
                            "ERROR: URL inválida: " + url
                    ));
                    valid = false;
                }

                if (projectKey ==null){
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.WARNING,
                            "Warning: falta el identificador de la aplicacion"));
                }

                if (projectID ==null){
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.ERROR,
                            "ERROR: falta el identificador de la aplicacion"));
                }
                if (projectName ==null){
                    result.addIssue(new CSVValidationError(
                            lineNumber,
                            CSVValidationError.Severity.ERROR,
                            "ERROR: falta el nombre de la aplicacion"));
                }

                if (valid) {
                    AppEntry entry = new AppEntry(
                            appKey,
                            identifier,
                            loginType,
                            headerName,
                            headerToken,
                            url,
                            projectKey,
                            projectID,
                            projectName
                    );
                    result.addValidEntry(entry);
                }
            }
        }

        return result;
    }

    private static String normalize(String[] values, int index) {
        if (index >= values.length) {
            return null;
        }

        String value = values[index].trim();
        return value.isEmpty() ? null : value;
    }
    
    
    // Validacion de la url independientemente de su protocolo.
    private static boolean isValidUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}