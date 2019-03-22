package org.challenge.core.adapaters;

import org.challenge.ws.RestClient;
import org.challenge.ws.RestClientFactory;
import org.challenge.ws.resource.Item;
import org.challenge.ws.resource.Option;
import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.Product;

import java.util.*;

public class RestServiceAdapterImpl implements RestServiceAdapter {

    private static RestServiceAdapterImpl INSTANCE = null;

    private RestServiceAdapterImpl(){
        super();
    }

    public static RestServiceAdapterImpl getInstance(){
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
        products.forEach(product -> map.put(product.getId(),product));
        return map;
    }

    @Override
    public Collection<Order> getOrders(String apiKey) {
        RestClient restClient = RestClientFactory.getInstance().create(apiKey);
        return restClient.getAllOrders();
    }

    @Override
    public boolean acceptOrder(String orderId) {
        // TODO acceptOrder
        return true;
    }

    @Override
    public boolean updateInventoryLevel(Set<Option> optionsToUpdate) {
        // TODO updateInventoryLevel
        return true;
    }

    @Override
    public boolean backorderingItems(Set<Item> items) {
        // TODO backorderingItems
        return true;
    }
}
