package org.challenge.ws.resource;

import org.challenge.util.DateUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement
public class Item {

    private String id;
    private String order_id;
    private String product_id;
    private String product_option_id;
    private int quantity;
    private String sku;
    private long price_cents;
    private String product_name;
    private String product_option_name;
    private boolean includes_tester;
    private long tester_price_cents;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_option_id() {
        return product_option_id;
    }

    public void setProduct_option_id(String product_option_id) {
        this.product_option_id = product_option_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public long getPrice_cents() {
        return price_cents;
    }

    public void setPrice_cents(long price_cents) {
        this.price_cents = price_cents;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_option_name() {
        return product_option_name;
    }

    public void setProduct_option_name(String product_option_name) {
        this.product_option_name = product_option_name;
    }

    public boolean isIncludes_tester() {
        return includes_tester;
    }

    public void setIncludes_tester(boolean includes_tester) {
        this.includes_tester = includes_tester;
    }

    public long getTester_price_cents() {
        return tester_price_cents;
    }

    public void setTester_price_cents(long tester_price_cents) {
        this.tester_price_cents = tester_price_cents;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = DateUtils.parse(created_at, DateUtils.ISO_8601);
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = DateUtils.parse(updated_at, DateUtils.ISO_8601);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return id != null ? id.equals(item.id) : item.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
