package org.challenge.core.adapaters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.challenge.util.DateUtils;
import org.challenge.ws.RestClient;
import org.challenge.ws.RestClientFactory;
import org.challenge.ws.resource.ItemBackorderingRequest;
import org.challenge.ws.resource.Option;
import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.Product;

import java.util.*;

public class RestServiceAdapterImpl implements RestServiceAdapter {

    private static RestServiceAdapterImpl INSTANCE = null;

    private RestServiceAdapterImpl() {
        super();
    }

    public static RestServiceAdapterImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RestServiceAdapterImpl();
        }
        return INSTANCE;
    }

    @Override
    public Map<String, Product> getMapProducts(String apiKey, String idBrand) {
        RestClient restClient = RestClientFactory.getInstance().create(apiKey);
        Collection<Product> products = restClient.getAllProductsByBrand(idBrand);
        Map<String, Product> map = new HashMap<>(products.size());
        products.forEach(product -> map.put(product.getId(), product));
        return map;
    }

    @Override
    public Collection<Order> getOrders(String apiKey) {
        RestClient restClient = RestClientFactory.getInstance().create(apiKey);
        return restClient.getAllOrders();
    }

    @Override
    public boolean acceptOrder(Order order, String apiKey) {
        RestClient rest = RestClientFactory.getInstance().create(apiKey);
        rest.acceptAnOrder(order);
        return true;
    }

    @Override
    public boolean updateInventoryLevel(Set<Option> optionsToUpdate, String apiKey) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();
        ((ObjectNode) rootNode).set("inventories", arrayNode);
        Iterator<Option> it = optionsToUpdate.iterator();
        while (it.hasNext()) {
            Option option = it.next();
            ObjectNode node = mapper.createObjectNode();
            node.put("sku", option.getSku());
            node.put("current_quantity", option.getAvailable_quantity());
            node.put("discontinued", option.isActive());
            node.put("backordered_until", option.getBackordered_until() == null ? null : DateUtils.toString(option.getBackordered_until(), DateUtils.ISO_8601));
            arrayNode.add(node);
        }
        String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        RestClient rest = RestClientFactory.getInstance().create(apiKey);
        rest.updateInventoryLevels(jsonStr);
        return true;
    }

    @Override
    public boolean backorderingItems(Set<ItemBackorderingRequest> items, String apiKey) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.createObjectNode();
        Iterator<ItemBackorderingRequest> it = items.iterator();
        while (it.hasNext()) {
            ItemBackorderingRequest item = it.next();
            ObjectNode node = mapper.createObjectNode();
            node.put("available_quantity", item.getAvailable_quantity());
            node.put("discontinued", item.isDiscontinued());
            if (item.getBackordered_until() != null) {
                node.put("backordered_until", DateUtils.toString(item.getBackordered_until(), DateUtils.ISO_8601));
            }
            ((ObjectNode) rootNode).set(item.getId(), node);
        }
        String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        RestClient rest = RestClientFactory.getInstance().create(apiKey);
        rest.backorderingItems(items.iterator().next().getIdOrder(), jsonStr);
        return true;
    }

}
