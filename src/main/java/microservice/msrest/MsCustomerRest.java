package microservice.msrest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import microservice.helper.RESTService;
import microservice.helper.SeleniumHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static microservice.helper.SeleniumHelper.printMethodName;

public class MsCustomerRest {

    private static final Logger log = LoggerFactory.getLogger(MsCustomerRest.class);

    public static void deleteCustomerByName(final String service, final String uri, String itemName) {
        log.info(printMethodName());

        try {
            Map<String, String> restItemMap = getCustomerIdsAndNamesThroughRestApi(service, uri);

            Iterator it = restItemMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                log.info("Map entry: "+pair.getKey() + " = " + pair.getValue());
                if (pair.getValue().equals(itemName)) {
                    RESTService.deleteFromUrl(service, uri+"/"+pair.getKey());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete existing customer from: " + service +"/"+ uri +" "+ itemName , e);
        }
    }

//    public static void deleteCustomerByNameByRestAssured(final String serviceUrl, final String uri, String customerEmail) {
//        printMethodName();
//
//        try {
//
//            RestAssured.baseURI = serviceUrl;
//
//            while (true) {
//                Response response = given().get(uri).then().log().ifError().extract().response();
//
//                //Get customer entry id by searching by email
//                Integer id = response.path("_embedded.customer.find { it.email == '"+customerEmail+"' }.id");
//
//                if (id == null) {
//                    log.info("No customer by email "+customerEmail+" found.");
//                    break;
//                }
//
//                given().delete (uri+"/"+id).then().log().ifError().statusCode(204).log().all();
//
//                log.info("Customer by email "+customerEmail+" deleted.");
//
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to delete existing catalog item " + serviceUrl +"/"+ uri +" "+ customerEmail , e);
//        }
//    }

    public static Map<String, String> getCustomerIdsAndNamesThroughRestApi(final String service, final String uri) {
        log.info(printMethodName());

        try {

            Map<String,String> itemMap = new HashMap<>();

            log.info("service: "+service);
            log.info("uri: "+uri);

            String jsonString = RESTService.getJsonFromUrl(service+ "/" + uri);

            ObjectMapper objectMapper = new ObjectMapper();

            //read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(jsonString);
            JsonNode ctl = rootNode.findPath("customer");

            Iterator<JsonNode> cltList = ctl.elements();
            while (cltList.hasNext()) {
                JsonNode catalogItem = cltList.next();
                String id = catalogItem.get("id").asText();
                String lastName = catalogItem.get("name").asText();
                String firstName = catalogItem.get("firstname").asText();
                String wholeName = firstName + " " + lastName;

                itemMap.put(id,wholeName);
            }
            return itemMap;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get customer items from Json: " + service +"/"+ uri, e);
        }
    }
}
