package _05_entities_billsPayment;

import javax.persistence.*;

@Entity
@Table(name = "_05_billing_details")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BillingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @ManyToOne
    private User owner;

    protected BillingDetail(String number, User owner) {
        this.number = number;
        this.owner = owner;
    }

    protected BillingDetail() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
