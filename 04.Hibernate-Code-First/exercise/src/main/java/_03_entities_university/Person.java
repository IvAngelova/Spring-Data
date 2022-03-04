package _03_entities_university;

import javax.persistence.*;

@Entity
@Table(name = "_03_people")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 70)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 70)
    private String lastName;
    @Column(name = "phone_number", unique = true, length = 40)
    private String phoneNumber;

    protected Person() {
    }

    protected Person(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
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

    public void setLastName(String last_name) {
        this.lastName = last_name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
