package org.challenge.ws.resource;

import org.challenge.util.DateUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement
public class Product {

    private String id;
    private String brand_id;
    private String short_description;
    private String description;
    private long wholesale_price_cents;
    private long retail_price_cents;
    private boolean active;
    private String name;
    private int unit_multiplier;
    private List<Option> options;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getWholesale_price_cents() {
        return wholesale_price_cents;
    }

    public void setWholesale_price_cents(long wholesale_price_cents) {
        this.wholesale_price_cents = wholesale_price_cents;
    }

    public long getRetail_price_cents() {
        return retail_price_cents;
    }

    public void setRetail_price_cents(long retail_price_cents) {
        this.retail_price_cents = retail_price_cents;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnit_multiplier() {
        return unit_multiplier;
    }

    public void setUnit_multiplier(int unit_multiplier) {
        this.unit_multiplier = unit_multiplier;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = DateUtils.parse(created_at,DateUtils.ISO_8601);
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = DateUtils.parse(updated_at,DateUtils.ISO_8601);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id != null ? id.equals(product.id) : product.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
