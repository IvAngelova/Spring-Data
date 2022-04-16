package softuni.exam.models.dtos;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketSeedDto {
    @XmlElement(name = "serial-number")
    @Size(min = 2)
    @Pattern(regexp = "^[0-9A-Za-z ]+$")
    private String serialNumber;

    @XmlElement
    @Positive
    private BigDecimal price;

    @XmlElement(name = "take-off")
    private String takeOff;

    @XmlElement(name = "from-town")
    private FromTownNameDto fromTown;

    @XmlElement(name = "to-town")
    private ToTownNameDto toTown;

    @XmlElement(name = "passenger")
    private PassengerEmailDto passenger;

    @XmlElement(name = "plane")
    private PlaneRegisterNumberDto plane;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(String takeOff) {
        this.takeOff = takeOff;
    }

    public FromTownNameDto getFromTown() {
        return fromTown;
    }

    public void setFromTown(FromTownNameDto fromTown) {
        this.fromTown = fromTown;
    }

    public ToTownNameDto getToTown() {
        return toTown;
    }

    public void setToTown(ToTownNameDto toTown) {
        this.toTown = toTown;
    }

    public PassengerEmailDto getPassenger() {
        return passenger;
    }

    public void setPassenger(PassengerEmailDto passenger) {
        this.passenger = passenger;
    }

    public PlaneRegisterNumberDto getPlane() {
        return plane;
    }

    public void setPlane(PlaneRegisterNumberDto plane) {
        this.plane = plane;
    }
}
