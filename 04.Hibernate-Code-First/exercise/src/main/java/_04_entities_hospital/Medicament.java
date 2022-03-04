package _04_entities_hospital;

import com.google.protobuf.DescriptorProtos;

import javax.persistence.*;

@Entity
@Table(name = "_04_medicaments")
public class Medicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String name;

    public Medicament() {
    }

    public Medicament(String name) {
        this.name = name;
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
