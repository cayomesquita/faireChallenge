package org.challenge.core;

import jersey.repackaged.com.google.common.collect.Maps;
import jersey.repackaged.com.google.common.collect.Sets;
import org.challenge.core.adapaters.RestServiceAdapter;
import org.challenge.core.adapaters.RestServiceAdapterImpl;
import org.challenge.ws.resource.Item;
import org.challenge.ws.resource.Option;
import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.Product;

import java.util.*;

public class Faire implements IFaire {

    private static Faire INSTANCE = null;

    private Map<String, Product> mapProducts = null;

    private Map<String, Integer> mapStateCount = new HashMap<>();
    private Map<Option, Integer> mapProductOptionSellingCount = new HashMap<>();
    private String largestOrder;
    private Long largestOrderAmount;

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
        Collection<Order> orders = getRestServiceAdapter().getOrders(apiKey);
        orders.forEach(order -> processOrder(order));
        printTopStateOrder();
        printTopProductOptionSelling();
        printLargestOrderAndAmount();
    }

    private void printLargestOrderAndAmount() {
        System.out.println(String.format("The largest order by dollar amount: %s", largestOrder));
        System.out.println(String.format("The largest order amount by dollar amount: %d", largestOrderAmount));
    }

    private void printTopProductOptionSelling() {
        String option = null;
        Integer max = -1;
        Iterator<Map.Entry<Option, Integer>> it = getMapProductOptionSellingCount().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Option, Integer> entry = it.next();
            if (entry.getValue() > max) {
                max = entry.getValue();
                option = entry.getKey().getId();
            }

        }
        System.out.println(String.format("The best selling product option: %s", option));
    }

    private void printTopStateOrder() {
        String topState = null;
        Integer max = -1;
        Iterator<Map.Entry<String, Integer>> it = getMapStateCount().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            if (entry.getValue() > max) {
                max = entry.getValue();
                topState = entry.getKey();
            }
        }
        System.out.println(String.format("The state with the most orders: %s", topState));
    }

    private void processOrder(Order order) {
        if (!isOrderAccepted(order)) {
            if (isOrderFulfilled(order)) {
                acceptOrder(order);
                updateInventoryLevels(order.getItems());
            } else {
                backorderingItems(order.getItems());
            }
        }
        countStates(order.getAddress().getState());
    }

    private void countStates(String stateName) {
        if (getMapStateCount().containsKey(stateName)) {
            getMapStateCount().put(stateName, getMapStateCount().get(stateName) + Integer.valueOf(1));
        } else {
            getMapStateCount().put(stateName, Integer.valueOf(1));
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
        long sellingAmount = 0;
        for (Item item : items) {
            if (getMapProducts().containsKey(item.getProduct_id())) {
                Product product = getMapProducts().get(item.getProduct_id());
                for (Option option : product.getOptions()) {
                    if (option.getId().equalsIgnoreCase(item.getProduct_option_id())) {
                        int qnt = item.getQuantity();
                        if (option.getAvailable_quantity() != null && qnt >= product.getUnit_multiplier() && qnt <= option.getAvailable_quantity()) {
                            option.setAvailable_quantity(option.getAvailable_quantity() - qnt);
                            optionsToUpdate.add(option);
                            countOption(option);
                            sellingAmount += product.getWholesale_price_cents() * qnt;
                        }
                    }
                }
            }
        }
        if (sellingAmount > largestOrderAmount) {
            largestOrderAmount = sellingAmount;
            largestOrder = items.iterator().next().getOrder_id();
        }
        if (!optionsToUpdate.isEmpty()) {
            getRestServiceAdapter().updateInventoryLevel(optionsToUpdate);
        }
    }

    private void countOption(Option option) {
        if (getMapProductOptionSellingCount().containsKey(option)) {
            getMapProductOptionSellingCount().put(option, getMapProductOptionSellingCount().get(option) + Integer.valueOf(1));
        } else {
            getMapProductOptionSellingCount().put(option, Integer.valueOf(1));
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

    public Map<String, Integer> getMapStateCount() {
        return mapStateCount;
    }

    public Map<Option, Integer> getMapProductOptionSellingCount() {
        return mapProductOptionSellingCount;
    }
}
