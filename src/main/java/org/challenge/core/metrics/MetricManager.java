package org.challenge.core.metrics;

import org.challenge.ws.resource.Item;
import org.challenge.ws.resource.Option;
import org.challenge.ws.resource.Order;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MetricManager {

    private Map<String, Option> mapProductOptions = null;

    private Map<String, Integer> mapStateCount = new HashMap<>();
    private Map<Option, Integer> mapProductOptionSellingCount = new HashMap<>();
    private String largestOrder;
    private long largestOrderAmount;

    private static MetricManager INSTANCE = null;
    private String bestSellingProductOption = null;
    private long totalAmount = 0;
    private int orderCount = 0;

    private MetricManager() {
        super();
    }

    public static MetricManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MetricManager();
        }
        return INSTANCE;
    }

    public void calculateBestSellingProductOption() {
        Integer max = -1;
        Iterator<Map.Entry<Option, Integer>> it = getMapProductOptionSellingCount().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Option, Integer> entry = it.next();
            Integer value = entry.getValue();
            if (value > max) {
                max = value;
                bestSellingProductOption = entry.getKey().getId();
            }
        }
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

    public void computeMapProduto(Order order) {
        countStates(order.getAddress().getState());
        List<Item> items = order.getItems();
        long sellingAmount = 0;
        for (Item item : items) {
            Option option = getMapProductOptions().get(item.getProduct_option_id());
            int qnt = item.getQuantity();
            computeOption(option, qnt);
            sellingAmount += item.getPrice_cents() * qnt;
        }
        computeOrderMetrics(items.iterator().next().getOrder_id(), sellingAmount);
        totalAmount+=sellingAmount;
        orderCount++;
    }

    private void computeOrderMetrics(String orderId, long sellingAmount) {
        if (sellingAmount > largestOrderAmount) {
            largestOrderAmount = sellingAmount;
            largestOrder = orderId;
        }
    }

    private void computeOption(Option option, int qnt) {
        if (getMapProductOptionSellingCount().containsKey(option)) {
            getMapProductOptionSellingCount().put(option, getMapProductOptionSellingCount().get(option) + Integer.valueOf(qnt));
        } else {
            getMapProductOptionSellingCount().put(option, Integer.valueOf(qnt));
        }
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
        printAmountMetrics();
    }

    private void printAmountMetrics() {
        System.out.println(String.format("The total amount: %d", totalAmount));
        System.out.println(String.format("The average amount per order: %d", totalAmount/orderCount));
        System.out.println(String.format("The total of order: %d", orderCount));
    }

    private void printLargestOrderAndAmount() {
        System.out.println(String.format("The largest order by dollar amount: %s", largestOrder));
        System.out.println(String.format("The largest order amount by dollar amount: %d", largestOrderAmount));
    }

    private void printTopProductOptionSelling() {
        calculateBestSellingProductOption();
        System.out.println(String.format("The best selling product option: %s", bestSellingProductOption));
    }

    private void printTopStateOrder() {
        System.out.println(String.format("The state with the most orders: %s", calculateMortStateOrdersFrom()));
    }


    private Map<String, Integer> getMapStateCount() {
        return mapStateCount;
    }

    private Map<String, Option> getMapProductOptions() {
        return mapProductOptions;
    }

    private Map<Option, Integer> getMapProductOptionSellingCount() {
        return mapProductOptionSellingCount;
    }

    public void computeProductOptions(Map<String, Option> mapProductOptions) {
        this.mapProductOptions = new HashMap<>(mapProductOptions);
    }
}
