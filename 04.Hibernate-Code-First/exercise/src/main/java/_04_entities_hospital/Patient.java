package _04_entities_hospital;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "_04_patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 60)
    private String lastName;

    @Column(nullable = false)
    private String address;

    @Column(unique = true, length = 60)
    private String email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Lob
    @Column
    private byte[] picture;

    @Column(name = "is_insured", nullable = false)
    private Boolean isInsured;

    @OneToMany(mappedBy = "patient", targetEntity = Visitation.class)
    List<Visitation> visitations;

    public Patient() {
    }

    public List<Visitation> getVisitations() {
        return visitations;
    }

    public void setVisitations(List<Visitation> visitations) {
        this.visitations = visitations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Boolean getIsInsured() {
        return isInsured;
    }

    public void setIsInsured(Boolean isInsured) {
        this.isInsured = isInsured;
    }
}
