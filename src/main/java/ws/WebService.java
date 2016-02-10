package ws;

import core.PluginCore;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;

public class WebService {
    PluginCore core;
    private ResteasyClient client;
    private ResteasyWebTarget target;

    public WebService(PluginCore core) {
        this.core = core;
    }

    @PostConstruct
    protected void init(String urlParam) {
        client = new ResteasyClientBuilder().build();
        target = client.target("http://jsonplaceholder.typicode.com/" + urlParam);
    }

    public Object get(String urlParam, Class outputClass) {
        init(urlParam);

        Response response = target.request().get();
        String value = response.readEntity(String.class);
        core.getLogger().info(value);
        response.close();

        return value;
    }
}
