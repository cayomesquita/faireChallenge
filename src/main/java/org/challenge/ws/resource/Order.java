package org.challenge.ws.resource;

import org.challenge.util.DateUtils;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement
public class Order {

    private String id;
    private String state;
    private List<Item> items;
    private List<Shipment> shipments;
    private Address address;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime ship_after;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
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

    public LocalDateTime getShip_after() {
        return ship_after;
    }

    public void setShip_after(LocalDateTime ship_after) {
        this.ship_after = ship_after;
    }

    public void setShip_after(String ship_after) {
        this.ship_after = DateUtils.parse(ship_after, DateUtils.ISO_8601);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return id != null ? id.equals(order.id) : order.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
