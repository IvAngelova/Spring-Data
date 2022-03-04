package _05_entities_billsPayment;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "_05_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 60)
    private String lastName;

    @Column(nullable = false, unique = true, length = 60)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", targetEntity = BillingDetail.class)
    private List<BillingDetail> billingDetails;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BillingDetail> getBillingDetails() {
        return billingDetails;
    }

    public void setBillingDetails(List<BillingDetail> billingDetails) {
        this.billingDetails = billingDetails;
    }
}
