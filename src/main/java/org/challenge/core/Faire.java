package org.challenge.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import jersey.repackaged.com.google.common.collect.Sets;
import org.challenge.core.adapaters.RestServiceAdapter;
import org.challenge.core.adapaters.RestServiceAdapterImpl;
import org.challenge.core.metrics.MetricManager;
import org.challenge.ws.resource.*;

import java.util.*;

public class Faire implements IFaire {

    private static Faire INSTANCE = null;

    private Map<String, Product> mapProducts = null;
    private Map<String, Option> mapProductOptions = null;

    private String apiKey = null;

    public static Faire getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Faire();
        }
        return INSTANCE;
    }

    private Faire() {
        super();
    }

    @Override
    public void execute(String apiKey, String idBrand) {
        this.apiKey = apiKey;
        mapProducts = getRestServiceAdapter().getMapProducts(apiKey, idBrand);
        mapProductOptions = createMapProductOptions(mapProducts);
        MetricManager.getInstance().computeProductOptions(mapProductOptions);
        Collection<Order> orders = getRestServiceAdapter().getOrders(apiKey);
        orders.forEach(order -> processOrder(order));
        MetricManager.getInstance().printMetrics();
    }

    private Map<String, Option> createMapProductOptions(Map<String, Product> mapProducts) {
        Map<String, Option> map = new HashMap<>();
        mapProducts.values().forEach(product -> product.getOptions().forEach(option -> map.put(option.getId(), option)));
        return map;
    }

    private void processOrder(Order order) {
        MetricManager.getInstance().computeMapProduto(order);
        if (!isOrderAccepted(order)) {
            if (isOrderFulfilled(order)) {
                acceptOrder(order);
                updateInventoryLevels(order.getItems());
            } else {
                backorderingItems(order.getItems());
            }
        }
    }

    private void backorderingItems(List<Item> items) {
        Set<ItemBackorderingRequest> optionsToBackorder = Sets.newHashSet();
        for (Item item : items) {
            if (getMapProductOptions().containsKey(item.getProduct_option_id())) {
                Option option = getMapProductOptions().get(item.getProduct_option_id());
                int qnt = item.getQuantity();
                if (option.getAvailable_quantity() == null || qnt > option.getAvailable_quantity()) {
                    ItemBackorderingRequest itemBackOrder = new ItemBackorderingRequest();
                    itemBackOrder.setId(item.getId());
                    itemBackOrder.setIdOrder(item.getOrder_id());
                    itemBackOrder.setAvailable_quantity(option.getAvailable_quantity() == null ? 0 : option.getAvailable_quantity());
                    itemBackOrder.setBackordered_until(option.getBackordered_until());
                    itemBackOrder.setDiscontinued(!option.isActive());
                    optionsToBackorder.add(itemBackOrder);
                }
            }
        }
        try {
            getRestServiceAdapter().backorderingItems(optionsToBackorder, apiKey);
        } catch (JsonProcessingException e) {
            // TODO: 25/03/19  handle exception
        }
    }

    private void updateInventoryLevels(List<Item> items) {
        Set<Option> optionsToUpdate = Sets.newHashSet();
        //long sellingAmount = 0;
        for (Item item : items) {
            if (getMapProducts().containsKey(item.getProduct_id()) && getMapProductOptions().containsKey(item.getProduct_option_id())) {
                Product product = getMapProducts().get(item.getProduct_id());
                Option option = getMapProductOptions().get(item.getProduct_option_id());
                int qnt = item.getQuantity();
                if (option.getAvailable_quantity() != null && qnt >= product.getUnit_multiplier() && qnt <= option.getAvailable_quantity()) {
                    option.setAvailable_quantity(option.getAvailable_quantity() - qnt);
                    optionsToUpdate.add(option);
                    //MetricManager.getInstance().computeOption(option, qnt);
                    //                  sellingAmount = item.getPrice_cents() * qnt;
                }
            }
        }
        //MetricManager.getInstance().computeLargestOrderMetrics(items.iterator().next().getOrder_id(), sellingAmount);
        if (!optionsToUpdate.isEmpty()) {
            try {
                getRestServiceAdapter().updateInventoryLevel(optionsToUpdate, apiKey);
            } catch (JsonProcessingException e) {
                // TODO: 25/03/19 handle exception
            }
        }
    }

    private boolean isOrderFulfilled(Order order) {
        for (Item item : order.getItems()) {
            if (!isItemInventoryLevelOK(item)) {
                return false;
            }
        }
        return true;
    }

    private boolean isItemInventoryLevelOK(Item item) {
        if (getMapProducts().containsKey(item.getProduct_id()) && getMapProductOptions().containsKey(item.getProduct_option_id())) {
            Product product = getMapProducts().get(item.getProduct_id());
            Option option = getMapProductOptions().get(item.getProduct_option_id());
            int qnt = item.getQuantity();
            return option.getAvailable_quantity() != null && qnt >= product.getUnit_multiplier() && qnt <= option.getAvailable_quantity();
        }
        return false;
    }

    private boolean acceptOrder(Order order) {
        order.setState(EnumOrderState.PROCESSING.name());
        return getRestServiceAdapter().acceptOrder(order, apiKey);
    }

    private boolean isOrderAccepted(Order order) {
        return !EnumOrderState.NEW.name().equalsIgnoreCase(order.getState());
    }

    private RestServiceAdapter getRestServiceAdapter() {
        return RestServiceAdapterImpl.getInstance();
    }

    private Map<String, Product> getMapProducts() {
        return mapProducts;
    }

    private Map<String, Option> getMapProductOptions() {
        return mapProductOptions;
    }
}
