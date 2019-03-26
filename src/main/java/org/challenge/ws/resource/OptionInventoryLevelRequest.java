package org.challenge.ws.resource;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement
public class OptionInventoryLevelRequest {

    private String sku;
    private Integer current_quantity;
    private boolean discontinued;
    private LocalDateTime backordered_until;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getCurrent_quantity() {
        return current_quantity;
    }

    public void setCurrent_quantity(Integer current_quantity) {
        this.current_quantity = current_quantity;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    public LocalDateTime getBackordered_until() {
        return backordered_until;
    }

    public void setBackordered_until(LocalDateTime backordered_until) {
        this.backordered_until = backordered_until;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptionInventoryLevelRequest that = (OptionInventoryLevelRequest) o;

        return sku != null ? sku.equals(that.sku) : that.sku == null;
    }

    @Override
    public int hashCode() {
        return sku != null ? sku.hashCode() : 0;
    }
}
