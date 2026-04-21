package zapConfiguration;

import com.capge.pack.ZapAuto;
import org.openqa.selenium.Proxy;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import tools.Logger;

import static com.capge.pack.ZapAuto.ZAP_PROXYHOST;
import static com.capge.pack.ZapAuto.ZAP_PROXYPORT;

public class ZapApiClient {

    protected ClientApi zapApp;

    public ZapApiClient() {
        this.zapApp = new ClientApi(ZAP_PROXYHOST, ZAP_PROXYPORT);
    }

    protected Proxy apiZapSetup() {
        Logger.startRequest();
        try {
            Logger.info(ZapApiClient.class, "ZAP Version: " + zapApp.core.version());
            Proxy seleniumProxy = new Proxy();
            seleniumProxy.setHttpProxy(ZAP_PROXYHOST + ":" + ZAP_PROXYPORT);
            seleniumProxy.setSslProxy(ZAP_PROXYHOST + ":" + ZAP_PROXYPORT);
            Logger.info(ZapApiClient.class, "Checking ZAP API availability");
            zapApp.core.newSession("", "");
            Logger.info(ZapApiClient.class, "New session created successfully");
            Logger.info(ZapApiClient.class, "Creating a new session");

            return seleniumProxy;

        } catch (Exception e) {
            Logger.error(ZapAuto.class, "Error Type: ", e);
            return null;

        } finally {
            Logger.clearRequest();
        }
    }

    public ClientApi getZapApp() {
        return zapApp;
    }
}
