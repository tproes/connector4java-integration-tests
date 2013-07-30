package de.osiam.client.helper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.apache.commons.io.FileUtils;
import org.osiam.client.oauth.AccessToken;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class OsiamITHelper {

    private final WebResource userResource;
    private final WebResource groupResource;

    public OsiamITHelper(URI userUrl, URI groupUrl, AccessToken accessToken) {
        this.userResource = createWebResource(userUrl, accessToken);
        this.groupResource = createWebResource(groupUrl, accessToken);
    }

    /**
     * Produce a User entry in the given service
     *
     * @param uuid
     */
    public void createUser(String uuid) throws URISyntaxException, IOException {
        URI path = this.getClass().getClassLoader().getResource(uuid + ".json").toURI();
        String payload = FileUtils.readFileToString(new File(path), "UTF-8");

        ClientResponse response = userResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, payload);

    }

    static WebResource createWebResource(URI path, AccessToken accessToken) {
        ClientConfig config = new DefaultClientConfig();
        config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(config);
        WebResource resource = client.resource(path);
        resource.accept(MediaType.APPLICATION_JSON);
        resource.header("Authorization", "Bearer " + accessToken.getToken());
        return resource;
    }


}
