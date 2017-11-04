package microservice.msrest;


import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gherkin.lexer.Th;
import microservice.helper.RESTService;
import org.joda.time.DateTime;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static microservice.helper.SeleniumHelper.printMethodName;


public class MsCatalogRest {

    public static void addCatalogItem(final String service, final String uri, final String itemName, final String itemPrice) {
        printMethodName();

        try {

            //String json = "{ \"name\" : \"kimmo\", \"price\" : \"99.0\" }";

            String userDir = System.getProperty("user.dir");

            String catalog_template = new String(Files.readAllBytes(Paths.get(userDir + "/microservice-demo-acceptance-tests/src/main/resources/acceptance-tests/catalog_template.json")));

            ObjectMapper objectMapper = new ObjectMapper();

            //create Json Root Node
            JsonNode rootNode = objectMapper.readTree(catalog_template);

            //update JSON data
            ((ObjectNode) rootNode).put("name", itemName);

            //update JSON data
            ((ObjectNode) rootNode).put("price", itemPrice);

            String json = objectMapper.writeValueAsString(rootNode);

            RESTService.postJsonToUrl(service,uri,json);

        } catch (Exception e) {
            throw new RuntimeException("Failed to add catalog item " + service + "/" + uri, e);
        }
    }

    public static JsonNode getSingleCatalogItemWithId(final String service, final String uri, final String id) {
        printMethodName();

        try {
            String jsonString = RESTService.getJsonFromUrl(service+"/"+uri+"/"+id);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readTree(jsonString);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get catalog item with id " + service +"/"+uri+"/"+id, e);
        }
    }

    public static JsonNode waitForGetSingleCatalogItemWithId(Integer timeout, Integer retryInterval, final String service, final String uri, final String id)  {
        printMethodName();

        DateTime timeoutTime = new DateTime().plusSeconds(timeout);
        while (timeoutTime.isAfterNow()) {

            try {
                return getSingleCatalogItemWithId (service,uri,id);

            } catch (Throwable e) {
                System.out.println("Got exception: " + e);
                System.out.println("Retry sleeping " + (retryInterval) + " seconds");
                Selenide.sleep(retryInterval * 1000);

            }
        }
        throw new RuntimeException("Getting single catalog item with id failed after " + timeout + " seconds.");
    }



    public static ArrayList getCatalogItemIdsThroughRestApi(final String service, final String uri) {
        printMethodName();

        try {

            ArrayList al = new ArrayList();

            String jsonString = RESTService.getJsonFromUrl(service+ "/" + uri);

            ObjectMapper objectMapper = new ObjectMapper();

            //read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode ctl = rootNode.findPath("catalog");

            System.out.println("\n==> Ids...");
            Iterator<JsonNode> cltList = ctl.elements();
            while (cltList.hasNext()) {
                al.add(cltList.next().get("id"));
            }
            return al;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get catalog items " + service + uri, e);
        }
    }

    public static Map<String, String> getCatalogItemIdsAndNamesThroughRestApi(final String service, final String uri) {
        printMethodName();

        try {

            Map<String,String> catalogIdNameMap = new HashMap<>();


            System.out.println("service: "+service);
            System.out.println("uri: "+uri);

            String jsonString = RESTService.getJsonFromUrl(service+ "/" + uri);

            ObjectMapper objectMapper = new ObjectMapper();

            //read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode ctl = rootNode.findPath("catalog");

            Iterator<JsonNode> cltList = ctl.elements();
            while (cltList.hasNext()) {
                JsonNode catalogItem = cltList.next();
                catalogIdNameMap.put(catalogItem.get("id").asText(),catalogItem.get("name").asText());
            }
            return catalogIdNameMap;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get catalog items " + service + uri, e);
        }
    }



    public static void deleteExistingCatalogItems(final String service, final String uri) {
        printMethodName();

        try {

            ArrayList catalogIds = getCatalogItemIdsThroughRestApi(service, uri);

            for (Object id : catalogIds) {
                //System.out.println(getJsonFromUrl(service+uri+"/"+id));
                RESTService.deleteFromUrl(service, uri+"/"+id);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete existing catalog items " + service + uri, e);
        }
    }

    public static void deleteCatalogItemByName(final String service, final String uri, String catalogItemName) {
        printMethodName();

        try {
            Map<String, String> catalogItemsMap = getCatalogItemIdsAndNamesThroughRestApi(service, uri);

            Iterator it = catalogItemsMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println("Map entry: "+pair.getKey() + " = " + pair.getValue());
                if (pair.getValue().equals(catalogItemName)) {
                    RESTService.deleteFromUrl(service, uri+"/"+pair.getKey());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete existing catalog item " + service +"/"+ uri +" "+ catalogItemName , e);
        }
    }



    public static String findCatalogItemNameById(final String service, final String uri, String catalogId) {
        printMethodName();
        try {

            JsonNode jsonNode = MsCatalogRest.getSingleCatalogItemWithId(service, uri, catalogId);
            String catalogName = jsonNode.get("name").asText();
            System.out.println(catalogName);

            return catalogName;

        } catch (Exception e) {
            throw new RuntimeException("Could not find catalog name by id " + service + uri + catalogId , e);
        }
    }

}
