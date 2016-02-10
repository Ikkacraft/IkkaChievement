package ws;

import core.PluginCore;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.JerseyClientBuilder;

public class WebService {
    PluginCore core;
    private Client client;
    private WebTarget target;

    public WebService(PluginCore core) {
        this.core = core;
    }

    @PostConstruct
    protected void init(String urlParam) {
        client = ClientBuilder.newClient();
        target = client.target("http://jsonplaceholder.typicode.com/" + urlParam);
    }

    public Object get(String urlParam, Class outputClass) {
        init(urlParam);
        return target.request(MediaType.APPLICATION_JSON).get(outputClass);
    }
}
