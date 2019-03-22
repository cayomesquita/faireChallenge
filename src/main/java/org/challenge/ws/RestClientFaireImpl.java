package org.challenge.ws;

import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.OrdersResponse;
import org.challenge.ws.resource.Product;
import org.challenge.ws.resource.ProductsResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.stream.Collectors;

public class RestClientFaireImpl implements RestClient {

    private static final String HEADER_KEY_NAME = "X-FAIRE-ACCESS-TOKEN";
    private static final String URL = "https://www.faire-stage.com/api/v1";
    private static final String LIMIT_QUERY_PARAM_NAME = "limit";
    private static final String PAGE_QUERY_PARAM_NAME = "page";

    private String key;

    protected RestClientFaireImpl(String apiKey){
        super();
        this.key = apiKey;
    }

    @Override
    public Collection<Product> getAllProductsByBrand(String brand) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(URL).path("products");
        ProductsResponse response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey()).get(ProductsResponse.class);
        Collection<Product> products = response.getProducts().stream().filter(product -> product.getBrand_id().equalsIgnoreCase(brand)).collect(Collectors.toSet());
        while (response.getLimit() == response.getProducts().size()){
            response = webTarget.queryParam(LIMIT_QUERY_PARAM_NAME, response.getLimit()).queryParam(PAGE_QUERY_PARAM_NAME, response.getPage() + 1)
                    .request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey()).get(ProductsResponse.class);
            products.addAll(response.getProducts().stream().filter(product -> product.getBrand_id().equalsIgnoreCase(brand)).collect(Collectors.toSet()));
        }
        return products;
    }

    @Override
    public Collection<Order> getAllOrders() {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(URL).path("orders");
        OrdersResponse response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey()).get(OrdersResponse.class);
        Collection<Order> orders = response.getOrders().stream().collect(Collectors.toSet());
        while (response.getLimit() == response.getOrders().size()){
            response = webTarget.queryParam(LIMIT_QUERY_PARAM_NAME, response.getLimit()).queryParam(PAGE_QUERY_PARAM_NAME, response.getPage() + 1)
                    .request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey()).get(OrdersResponse.class);
            orders.addAll(response.getOrders());
        }
        return orders;
    }

    @Override
    public void acceptAnOrder(String idOrder) {

    }

    @Override
    public void updateInventoryLevels() {

    }

    @Override
    public void backorderingItems() {

    }

    public String getKey() {
        return key;
    }

}
