package org.challenge.core.adapaters;

import org.challenge.ws.resource.Item;
import org.challenge.ws.resource.Option;
import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.Product;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface RestServiceAdapter {

    Map<String, Product> getMapProducts(String apiKey, String idBrand);

    Collection<Order> getOrders(String apiKey);

    boolean acceptOrder(String orderId);

    boolean updateInventoryLevel(Set<Option> optionsToUpdate);

    boolean backorderingItems(Set<Item> items);
}
