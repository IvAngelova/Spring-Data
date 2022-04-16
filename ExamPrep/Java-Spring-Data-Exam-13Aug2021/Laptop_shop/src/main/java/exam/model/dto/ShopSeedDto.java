package exam.model.dto;

import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "shop")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShopSeedDto {
    @XmlElement
    @Size(min = 4)
    @NotBlank
    private String name;

    @XmlElement
    @Min(value = 20000)
    @NotNull
    private BigDecimal income;

    @XmlElement
    @Size(min = 4)
    @NotBlank
    private String address;

    @XmlElement(name = "employee-count")
    @Min(1)
    @Max(50)
    @NotNull
    private Integer employeeCount;

    @XmlElement(name = "shop-area")
    @Min(150)
    @NotNull
    private Integer shopArea;

    @XmlElement(name = "town")
    @NotNull
    private ShopTownDto town;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    public Integer getShopArea() {
        return shopArea;
    }

    public void setShopArea(Integer shopArea) {
        this.shopArea = shopArea;
    }

    public ShopTownDto getTown() {
        return town;
    }

    public void setTown(ShopTownDto town) {
        this.town = town;
    }
}
