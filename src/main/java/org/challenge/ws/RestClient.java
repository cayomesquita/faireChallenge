package org.challenge.ws;

import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.Product;

import java.util.Collection;

public interface RestClient {

    Collection<Product> getAllProductsByBrand(String brand);

    Collection<Order> getAllOrders();

    void acceptAnOrder(String idOrder);

    void updateInventoryLevels();

    void backorderingItems();
}
