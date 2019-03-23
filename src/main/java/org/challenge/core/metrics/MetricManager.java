package org.challenge.core.metrics;

import org.challenge.ws.resource.Item;
import org.challenge.ws.resource.Option;
import org.challenge.ws.resource.Order;
import org.challenge.ws.resource.Product;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MetricManager {

    private Map<String, Product> mapProducts = null;
    private Map<String, Integer> mapStateCount = new HashMap<>();
    private Map<Option, Integer> mapProductOptionSellingCount = new HashMap<>();
    private String largestOrder;
    private long largestOrderAmount;

    private static MetricManager INSTANCE = null;

    private MetricManager() {
        super();
    }

    public static MetricManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MetricManager();
        }
        return INSTANCE;
    }

    public String calculateBestSellingProductOption() {
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
        return option;
    }

    public String calculateMortStateOrdersFrom() {
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
        return topState;
    }

    public void compute(Order order) {
        countStates(order.getAddress().getState());
        List<Item> items = order.getItems();
        long sellingAmount = 0;
        for (Item item : items) {
            Product product = getMapProducts().get(item.getProduct_id());
            for (Option option : product.getOptions()) {
                if (option.getId().equalsIgnoreCase(item.getProduct_option_id())) {
                    int qnt = item.getQuantity();
                    computeOption(option, qnt);
                    sellingAmount += item.getPrice_cents() * qnt;
                }
            }
        }
        computeLargestOrderMetrics(items.iterator().next().getOrder_id(), sellingAmount);
    }

    public void computeLargestOrderMetrics(String orderId, long sellingAmount){
        if (sellingAmount > largestOrderAmount) {
            largestOrderAmount = sellingAmount;
            largestOrder = orderId;
        }
    }

    public void computeOption(Option option, int qnt) {
        if (getMapProductOptionSellingCount().containsKey(option)) {
            getMapProductOptionSellingCount().put(option, getMapProductOptionSellingCount().get(option) + Integer.valueOf(qnt));
        } else {
            getMapProductOptionSellingCount().put(option, Integer.valueOf(qnt));
        }
        getMapProductOptionSellingCount().get(option);
    }

    private void countStates(String stateName) {
        if (getMapStateCount().containsKey(stateName)) {
            getMapStateCount().put(stateName, getMapStateCount().get(stateName) + Integer.valueOf(1));
        } else {
            getMapStateCount().put(stateName, Integer.valueOf(1));
        }
    }

    public void printMetrics() {
        printTopStateOrder();
        printTopProductOptionSelling();
        printLargestOrderAndAmount();
    }

    private void printLargestOrderAndAmount() {
        System.out.println(String.format("The largest order by dollar amount: %s", largestOrder));
        System.out.println(String.format("The largest order amount by dollar amount: %d", largestOrderAmount));
    }

    private void printTopProductOptionSelling() {
        System.out.println(String.format("The best selling product option: %s", calculateBestSellingProductOption()));
    }

    private void printTopStateOrder() {
        System.out.println(String.format("The state with the most orders: %s", calculateMortStateOrdersFrom()));
    }


    public Map<String, Integer> getMapStateCount() {
        return mapStateCount;
    }

    public Map<String, Product> getMapProducts() {
        return mapProducts;
    }

    public Map<Option, Integer> getMapProductOptionSellingCount() {
        return mapProductOptionSellingCount;
    }

    public void compute(Map<String, Product> mapProducts) {
        this.mapProducts = mapProducts;
    }
}
