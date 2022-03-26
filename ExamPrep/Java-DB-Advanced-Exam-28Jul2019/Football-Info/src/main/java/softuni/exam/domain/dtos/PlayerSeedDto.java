package softuni.exam.domain.dtos;

import com.google.gson.annotations.Expose;
import softuni.exam.domain.entities.Position;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class PlayerSeedDto {
    @Expose
    @NotBlank
    private String firstName;

    @Expose
    @Size(min = 3, max = 15)
    private String lastName;

    @Expose
    @Min(value = 1)
    @Max(value = 99)
    private Integer number;

    @Expose
    @Min(value = 0)
    private BigDecimal salary;

    @Expose
    @NotNull
    private Position position;

    @Expose
    @NotNull
    private PictureUrlDto picture;

    @Expose
    @NotNull
    private TeamNameDto team;

    public PlayerSeedDto() {

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PictureUrlDto getPicture() {
        return picture;
    }

    public void setPicture(PictureUrlDto picture) {
        this.picture = picture;
    }

    public TeamNameDto getTeam() {
        return team;
    }

    public void setTeam(TeamNameDto team) {
        this.team = team;
    }
}
