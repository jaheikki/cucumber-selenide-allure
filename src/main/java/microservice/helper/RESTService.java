package microservice.helper;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static microservice.helper.SeleniumHelper.printMethodName;

public class RESTService {

    private static final Logger log = LoggerFactory.getLogger(RESTService.class);

    public static String getJsonFromUrl(final String url) {
        log.info(printMethodName());

        try {

            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);

            Client client = Client.create(clientConfig);
            client.addFilter(new LoggingFilter(System.out));

            WebResource webResource = client.resource(url);

            ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

            log.info("Response status:" + response.getStatus());

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            return response.getEntity(String.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get from url: " + url, e);
        }
    }

    public static void postJsonToUrl(final String service, final String uri,final String json) {
        log.info(printMethodName());

        try {

            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);

            Client client = Client.create(clientConfig);
            client.addFilter(new LoggingFilter(System.out));
            //client.addFilter(new HTTPBasicAuthFilter("user", "password"));

            WebResource webResource = client.resource(service + "/" + uri);

            ClientResponse response = webResource
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class,json);

            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to post Json to url " + service + "/" + uri, e);
        }
    }

    public static void deleteFromUrl(final String service, final String uri) {
        log.info(printMethodName());

        try {

            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getClasses().add(JacksonJaxbJsonProvider.class);

            Client client = Client.create(clientConfig);
            client.addFilter(new LoggingFilter(System.out));

            WebResource webResource = client.resource(service + "/" + uri);

            ClientResponse response = webResource
                    .type(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .delete(ClientResponse.class);

            if (response.getStatus() != 204) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete from url " + service + "/" + uri, e);
        }
    }

}
