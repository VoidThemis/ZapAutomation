package csvHandler;

public class AppEntry {
    private String appKey;
    private String CI;
    private String loginType;
    private String headerName;
    private String headerToken;
    private String preproductionUrl;
    private String projectKey;
    private String projectID;
    private String projectName;

    // Constructor
    public AppEntry(String appKey, String identifier, String loginType, String headerName, String headerToken, String url,
                    String projectKey, String projectID, String projectName) {
        this.appKey = appKey;
        this.CI = identifier;
        this.loginType = loginType;
        this.headerName = headerName;
        this.headerToken = headerToken;
        this.preproductionUrl = url;
        this.projectKey = projectKey;
        this.projectID = projectID;
        this.projectName = projectName;
    }

    // Getters
    public String getAppKey() { return appKey; }
    public String getCI() { return CI; }
    public String getLoginType() { return loginType; }
    public String getHeaderName() { return headerName; }
    public String getHeaderToken() { return headerToken; }
    public String getPreproductionUrl() { return preproductionUrl;}
    public String getProjectKey() { return projectKey; }
    public String getProjectID() { return projectID; }
    public String getProjectName() { return projectName;}

    @Override
    public String toString() {
        return "AppEntry{" +
                "AppKey='" + appKey + '\'' +
                ", CI='" + CI + '\'' +
                ", LoginType='" + loginType + '\'' +
                ", headerName='" + headerName + '\'' +
                ", headerToken='" + headerToken + '\'' +
                ", PreproductionUrl='" + preproductionUrl + '\'' +
                ", ProjectKey='" + projectKey + '\'' +
                ", ProjectID='" + projectID + '\'' +
                ", ProjectName='" + projectName + '\'' +
                '}';
    }


}