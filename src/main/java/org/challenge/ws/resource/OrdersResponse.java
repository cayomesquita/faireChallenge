package org.challenge.ws.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class OrdersResponse {

    private int page;

    private int limit;

    private List<Order> orders;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
