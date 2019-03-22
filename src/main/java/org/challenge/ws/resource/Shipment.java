package org.challenge.ws.resource;

import org.challenge.util.DateUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@XmlRootElement
public class Shipment {

    private String id;
    private String order_id;
    private String carrier;
    private String tracking_code;
    private long maker_cost_cents;
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

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getTracking_code() {
        return tracking_code;
    }

    public void setTracking_code(String tracking_code) {
        this.tracking_code = tracking_code;
    }

    public long getMaker_cost_cents() {
        return maker_cost_cents;
    }

    public void setMaker_cost_cents(long maker_cost_cents) {
        this.maker_cost_cents = maker_cost_cents;
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

        Shipment shipment = (Shipment) o;

        return id != null ? id.equals(shipment.id) : shipment.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
