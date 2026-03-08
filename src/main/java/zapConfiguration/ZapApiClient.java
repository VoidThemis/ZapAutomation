package zapConfiguration;

import com.capge.pack.ZapAuto;
import org.openqa.selenium.Proxy;
import org.zaproxy.clientapi.core.ClientApi;
import tools.Logger;

import static com.capge.pack.ZapAuto.ZAP_PROXYHOST;
import static com.capge.pack.ZapAuto.ZAP_PROXYPORT;

public class ZapApiClient {

    protected ClientApi zapApp;

    public void initializeClient() {
        this.zapApp = new ClientApi(ZAP_PROXYHOST, ZAP_PROXYPORT);
    }

    protected Proxy apiZapSetup() {
        Logger.startRequest();
        try {
            Proxy seleniumProxy = new Proxy();
            seleniumProxy.setProxyAutoconfigUrl("http://" + ZAP_PROXYHOST + ":" + ZAP_PROXYPORT);
            zapApp.ascan.removeAllScans();
            zapApp.core.newSession("","");
            return seleniumProxy;
        }catch (Exception e){
            Logger.error(ZapAuto.class, "Error Type: ", e);
            return null;
        }finally {
            Logger.clearRequest();
        }
    }

    public ClientApi getZapApp() {
        return zapApp;
    }
}
