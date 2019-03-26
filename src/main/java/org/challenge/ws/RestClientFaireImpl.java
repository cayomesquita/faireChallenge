package org.challenge.ws;

import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.OrdersResponse;
import org.challenge.ws.resource.Product;
import org.challenge.ws.resource.ProductsResponse;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

public class RestClientFaireImpl implements RestClient {

    private static final String HEADER_KEY_NAME = "X-FAIRE-ACCESS-TOKEN";
    private static final String URL = "https://www.faire-stage.com/api/v1";
    private static final String LIMIT_QUERY_PARAM_NAME = "limit";
    private static final String PAGE_QUERY_PARAM_NAME = "page";
    private static final String PATCH_METHOD = "PATCH";

    private String key;

    protected RestClientFaireImpl(String apiKey) {
        super();
        this.key = apiKey;
    }

    @Override
    public Collection<Product> getAllProductsByBrand(String brand) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(URL).path("products");
        ProductsResponse response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey()).get(ProductsResponse.class);
        Collection<Product> products = response.getProducts().stream().filter(product -> product.getBrand_id().equalsIgnoreCase(brand)).collect(Collectors.toSet());
        while (response.getLimit() == response.getProducts().size()) {
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
        while (response.getLimit() == response.getOrders().size()) {
            response = webTarget.queryParam(LIMIT_QUERY_PARAM_NAME, response.getLimit()).queryParam(PAGE_QUERY_PARAM_NAME, response.getPage() + 1)
                    .request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey()).get(OrdersResponse.class);
            orders.addAll(response.getOrders());
        }
        return orders;
    }

    @Override
    public void acceptAnOrder(Order order) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(URL).path("orders").path(order.getId()).path("processing");
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey()).put(Entity.entity(order,MediaType.APPLICATION_JSON_TYPE));
        if (Response.Status.OK.getStatusCode() != response.getStatus()) {
            // Handle backorderingItems Response.Status.OK
        }
        response.close();
    }

    @Override
    public void updateInventoryLevels(String jsonContent) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(URL).path("products/options/inventory-levels");
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey())
                .build(PATCH_METHOD, Entity.entity(jsonContent, MediaType.APPLICATION_JSON_TYPE)).property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true).invoke();
        if (Response.Status.OK.getStatusCode() != response.getStatus()) {
            // Handle backorderingItems Response.Status.OK
        }
        response.close();
    }

    @Override
    public void backorderingItems(String idOrder, String jsonContent) {
        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(URL).path("orders").path(idOrder).path("items/availability");
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).header(HEADER_KEY_NAME, getKey()).post(Entity.entity(jsonContent, MediaType.APPLICATION_JSON_TYPE));
        if (Response.Status.OK.getStatusCode() != response.getStatus()) {
            // Handle backorderingItems Response.Status.OK
        }
        response.close();
    }

    public String getKey() {
        return key;
    }

}
