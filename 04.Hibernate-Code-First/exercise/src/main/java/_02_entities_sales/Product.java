package _02_entities_sales;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "_02_products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "product", targetEntity = Sale.class)
    private Set<Sale> sales;

    public Product() {
        this.sales = new HashSet<>();
    }

    public Product(String name, int quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.sales = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Sale> getSales() {
        return sales;
    }

    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }
}
