package microservice.msrest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.helper.RESTService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static microservice.helper.SeleniumHelper.printMethodName;

public class MsCustomerRest {
    public static void deleteCustomerByName(final String service, final String uri, String itemName) {
        printMethodName();

        try {
            Map<String, String> restItemMap = getCustomerIdsAndNamesThroughRestApi(service, uri);

            Iterator it = restItemMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println("Map entry: "+pair.getKey() + " = " + pair.getValue());
                if (pair.getValue().equals(itemName)) {
                    RESTService.deleteFromUrl(service, uri+"/"+pair.getKey());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete existing customer from: " + service +"/"+ uri +" "+ itemName , e);
        }
    }

    public static Map<String, String> getCustomerIdsAndNamesThroughRestApi(final String service, final String uri) {
        printMethodName();

        try {

            Map<String,String> itemMap = new HashMap<>();

            System.out.println("service: "+service);
            System.out.println("uri: "+uri);

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
