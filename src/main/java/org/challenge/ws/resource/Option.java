package org.challenge.ws.resource;

import org.challenge.util.DateUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement
public class Option {

    private String id;
    private String product_id;
    private boolean active;
    private String name;
    private String sku;
    private Integer available_quantity;
    private LocalDateTime backordered_until;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getAvailable_quantity() {
        return available_quantity;
    }

    public void setAvailable_quantity(Integer available_quantity) {
        this.available_quantity = available_quantity;
    }

    public LocalDateTime getBackordered_until() {
        return backordered_until;
    }

    public void setBackordered_until(LocalDateTime backordered_until) {
        this.backordered_until = backordered_until;
    }

    public void setBackordered_until(String backordered_until) {
        this.backordered_until = DateUtils.parse(backordered_until,DateUtils.ISO_8601);
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

        Option option = (Option) o;

        if (id != null ? !id.equals(option.id) : option.id != null) return false;
        return product_id != null ? product_id.equals(option.product_id) : option.product_id == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (product_id != null ? product_id.hashCode() : 0);
        return result;
    }
}
