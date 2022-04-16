package hiberspring.domain.dtos;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class EmployeeSeedDto {
    @NotBlank
    @XmlAttribute(name = "first-name")
    private String firstName;

    @NotBlank
    @XmlAttribute(name = "last-name")
    private String lastName;

    @NotBlank
    @XmlAttribute(name = "position")
    private String position;

    @NotBlank
    @XmlElement
    private String card;

    @NotBlank
    @XmlElement
    private String branch;

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

}
