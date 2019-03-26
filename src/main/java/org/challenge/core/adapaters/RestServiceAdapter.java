package org.challenge.core.adapaters;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.challenge.ws.resource.ItemBackorderingRequest;
import org.challenge.ws.resource.Option;
import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.Product;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface RestServiceAdapter {

    Map<String, Product> getMapProducts(String apiKey, String idBrand);

    Collection<Order> getOrders(String apiKey);

    boolean acceptOrder(Order order, String apiKey);

    boolean updateInventoryLevel(Set<Option> optionsToUpdate, String apiKey) throws JsonProcessingException;

    boolean backorderingItems(Set<ItemBackorderingRequest> optionsToBackorder, String apiKey) throws JsonProcessingException;
}
