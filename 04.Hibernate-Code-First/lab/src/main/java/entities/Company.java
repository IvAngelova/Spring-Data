package entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "company", targetEntity = Plane.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Plane> planes;


    public Company() {
        this.planes = new HashSet<>();
    }

    public Company(String name) {
        this.name = name;
        this.planes = new HashSet<>();
    }


    public Set<Plane> getPlanes() {
        return Collections.unmodifiableSet(planes);
    }

    public void setPlanes(Set<Plane> planes) {
        this.planes = planes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
