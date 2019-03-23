package org.challenge.core;

import jersey.repackaged.com.google.common.collect.Sets;
import org.challenge.core.adapaters.RestServiceAdapter;
import org.challenge.core.adapaters.RestServiceAdapterImpl;
import org.challenge.core.metrics.MetricManager;
import org.challenge.ws.resource.Item;
import org.challenge.ws.resource.Option;
import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.Product;

import java.util.*;

public class Faire implements IFaire {

    private static Faire INSTANCE = null;

    private Map<String, Product> mapProducts = null;

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
        mapProducts = getRestServiceAdapter().getMapProducts(apiKey, idBrand);
        MetricManager.getInstance().compute(mapProducts);
        Collection<Order> orders = getRestServiceAdapter().getOrders(apiKey);
        orders.forEach(order -> processOrder(order));
        MetricManager.getInstance().printMetrics();
    }

    private void processOrder(Order order) {
        MetricManager.getInstance().compute(order);
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
        Set<Item> optionsToBackorder = Sets.newHashSet();
        for (Item item : items) {
            if (getMapProducts().containsKey(item.getProduct_id())) {
                Product product = getMapProducts().get(item.getProduct_id());
                for (Option option : product.getOptions()) {
                    if (option.getId().equalsIgnoreCase(item.getProduct_option_id())) {
                        int qnt = item.getQuantity();
                        if (option.getAvailable_quantity() == null || qnt > option.getAvailable_quantity()) {
                            optionsToBackorder.add(item);
                        }
                    }
                }
            }
        }
        getRestServiceAdapter().backorderingItems(optionsToBackorder);
    }

    private void updateInventoryLevels(List<Item> items) {
        Set<Option> optionsToUpdate = Sets.newHashSet();
        //long sellingAmount = 0;
        for (Item item : items) {
            if (getMapProducts().containsKey(item.getProduct_id())) {
                Product product = getMapProducts().get(item.getProduct_id());
                for (Option option : product.getOptions()) {
                    if (option.getId().equalsIgnoreCase(item.getProduct_option_id())) {
                        int qnt = item.getQuantity();
                        if (option.getAvailable_quantity() != null && qnt >= product.getUnit_multiplier() && qnt <= option.getAvailable_quantity()) {
                            option.setAvailable_quantity(option.getAvailable_quantity() - qnt);
                            optionsToUpdate.add(option);
                            //MetricManager.getInstance().computeOption(option, qnt);
          //                  sellingAmount = item.getPrice_cents() * qnt;
                        }
                    }
                }
            }
        }
        //MetricManager.getInstance().computeLargestOrderMetrics(items.iterator().next().getOrder_id(), sellingAmount);
        if (!optionsToUpdate.isEmpty()) {
            getRestServiceAdapter().updateInventoryLevel(optionsToUpdate);
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
        if (getMapProducts().containsKey(item.getProduct_id())) {
            Product product = getMapProducts().get(item.getProduct_id());
            for (Option option : product.getOptions()) {
                if (option.getId().equalsIgnoreCase(item.getProduct_option_id())) {
                    int qnt = item.getQuantity();
                    return option.getAvailable_quantity() != null && qnt >= product.getUnit_multiplier() && qnt <= option.getAvailable_quantity();
                }
            }
        }
        return false;
    }

    private boolean acceptOrder(Order order) {
        order.setState(EnumOrderState.PROCESSING.name());
        return getRestServiceAdapter().acceptOrder(order.getId());
    }

    private boolean isOrderAccepted(Order order) {
        return !EnumOrderState.NEW.name().equalsIgnoreCase(order.getState());
    }

    private RestServiceAdapter getRestServiceAdapter() {
        return RestServiceAdapterImpl.getInstance();
    }

    public Map<String, Product> getMapProducts() {
        return mapProducts;
    }

}
