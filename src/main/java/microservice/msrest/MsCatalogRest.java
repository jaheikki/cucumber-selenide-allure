package microservice.msrest;


import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
//import gherkin.lexer.Th;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import microservice.helper.RESTService;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static microservice.helper.SeleniumHelper.printMethodName;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.Matchers.is;


public class MsCatalogRest {

    public static void addCatalogItem(final String service, final String uri, final String id, final String itemName, final String itemPrice) {
        printMethodName();

        try {
            //String json = "{ \"name\" : \"kimmo\", \"price\" : \"99.0\" }";

            String jsonTemplate = "{ \"id\" : \"${CATALOG_ID}\", \"name\" : \"${CATALOG_ITEM}\", \"price\" : \"${CATALOG_PRICE}\" }";
            String userDir = System.getProperty("user.dir");

            ObjectMapper objectMapper = new ObjectMapper();

            //create Json Root Node
            JsonNode rootNode = objectMapper.readTree(jsonTemplate);

            //update JSON data
            ((ObjectNode) rootNode).put("id", id);

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

    public static void addCatalogItemByRestAssured(final String service, final String uri, final String id, final String itemName, final String itemPrice) {
        printMethodName();

        try {

            RestAssured.baseURI = service;
            RequestSpecification request = RestAssured.given();

            JSONObject requestParams = new JSONObject();
            requestParams.put("id", id);
            requestParams.put("name", itemName);
            requestParams.put("price", itemPrice);

            // Add a header stating the Request body is a JSON
            request.header("Content-Type", "application/json");

            // Add the Json to the body of the request
            request.body(requestParams.toJSONString());

            // Post the request and check the response
            Response response = request.post("/"+uri);


        } catch (Exception e) {
            throw new RuntimeException("Failed to add catalog item by RestAssured" + service + "/" + uri, e);
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

        //Just for testing:
        //MsCatalogRest.addCatalogItem(serviceUrl,uri,"10","Termo", "34");
        //MsCatalogRest.addCatalogItem(serviceUrl,uri,"11","Termo", "35");
        //MsCatalogRest.addCatalogItem(serviceUrl,uri,"12","Termo", "36");

        try {
            Map<String, String> catalogItemsMap = getCatalogItemIdsAndNamesThroughRestApi(service, uri);

            //Looping to delete all duplicate (if exist) catalogitems
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

//    public static void deleteCatalogItemByRestAssured(final String serviceUrl, final String uri, String catalogItemName) {
//        printMethodName();
//
//        //Just for testing this method:
//        //MsCatalogRest.addCatalogItemByRestAssured(serviceUrl,uri,"15","Torspo", "99");
//        //MsCatalogRest.addCatalogItemByRestAssured(serviceUrl,uri,"16","Goose", "100");
//        //MsCatalogRest.addCatalogItemByRestAssured(serviceUrl,uri,"17","Goose", "101");
//        try {
//            RestAssured.baseURI = serviceUrl;
//
//            //Looping to delete all duplicate (if exist) catalogitems
//            while (true) {
//                Response response  = given().get(uri).then().log().ifError().extract().response();
//
//                Integer id = response.path("_embedded.catalog.find { it.name == '"+catalogItemName+"' }.id");
//
//                if (id == null) {
//                    System.out.println("No catalog item "+catalogItemName+" found.");
//                    break;
//                }
//
//                given().delete (uri +"/"+id).then().log().ifError().statusCode(204).log().all();
//
//                System.out.println("Catalog item "+catalogItemName+" deleted.");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete existing catalog item " + serviceUrl +"/"+ uri +" "+ catalogItemName , e);
//        }
//    }


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
