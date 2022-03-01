package entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @ManyToMany
    @JoinTable(name = "drivers_trucks",
    joinColumns = @JoinColumn(name = "driver_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "truck_id", referencedColumnName = "id"))
    private Set<Truck> trucks;


    public Driver() {
        this.trucks = new HashSet<>();
    }

    public Driver(String fullName) {
        this.fullName = fullName;
        this.trucks = new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Set<Truck> getTrucks() {
        return Collections.unmodifiableSet(trucks);
    }

    public void setTrucks(Set<Truck> trucks) {
        this.trucks = trucks;
    }
}
