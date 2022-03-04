package _02_entities_sales;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_02_store_locations")
public class StoreLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @OneToMany(mappedBy = "storeLocation", targetEntity = Sale.class)
    private Set<Sale> sales;

    public StoreLocation() {
        this.sales = new HashSet<>();
    }

    public StoreLocation(String locationName) {
        this.locationName = locationName;
        this.sales = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }


    public Set<Sale> getSales() {
        return sales;
    }

    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }
}
