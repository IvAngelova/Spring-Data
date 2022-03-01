package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trucks")
public class Truck extends Vehicle {
    @Column(name = "load_capacity", columnDefinition = "double")
    private Double loadCapacity;

    @ManyToMany(mappedBy = "trucks", targetEntity = Driver.class)
    private Set<Driver> drivers;

    public Truck() {
        this.drivers = new HashSet<>();
    }

    public Truck(String type, String model, BigDecimal price, String fuelType, Double loadCapacity) {
        super(type, model, price, fuelType);
        this.loadCapacity = loadCapacity;
        this.drivers = new HashSet<>();
    }

    public Double getLoadCapacity() {
        return loadCapacity;
    }

    public void setLoadCapacity(Double loadCapacity) {
        this.loadCapacity = loadCapacity;
    }

    public Set<Driver> getDrivers() {
        return Collections.unmodifiableSet(drivers);
    }

    public void setDrivers(Set<Driver> drivers) {
        this.drivers = drivers;
    }
}
