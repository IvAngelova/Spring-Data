package com.example.football.models.dto;

import com.example.football.models.entity.Position;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "player")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerSeedDto {
    @XmlElement(name = "first-name")
    @Size(min = 3)
    @NotBlank
    private String firstName;

    @XmlElement(name = "last-name")
    @Size(min = 3)
    @NotBlank
    private String lastName;

    @XmlElement
    @Email
    @NotBlank
    private String email;

    @XmlElement(name = "birth-date")
    @NotBlank
    private String birthDate;

    @XmlElement
    @NotNull
    private Position position;

    @XmlElement(name = "town")
    @NotNull
    private PlayerTownDto town;

    @XmlElement(name = "stat")
    @NotNull
    private PlayerStatDto stat;

    @XmlElement(name = "team")
    @NotNull
    private PlayerTeamDto team;

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public PlayerTownDto getTown() {
        return town;
    }

    public void setTown(PlayerTownDto town) {
        this.town = town;
    }

    public PlayerStatDto getStat() {
        return stat;
    }

    public void setStat(PlayerStatDto stat) {
        this.stat = stat;
    }

    public PlayerTeamDto getTeam() {
        return team;
    }

    public void setTeam(PlayerTeamDto team) {
        this.team = team;
    }
}
