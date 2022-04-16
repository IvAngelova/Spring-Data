package exam.model.dto;

import com.google.gson.annotations.Expose;
import exam.model.entity.Town;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class CustomerSeedDto {
    @Expose
    @Size(min = 2)
    @NotBlank
    private String firstName;

    @Expose
    @Size(min = 2)
    @NotBlank
    private String lastName;

    @Expose
    @Email
    @NotBlank
    private String email;

    @Expose
    @NotBlank
    private String registeredOn;

    @Expose
    @NotNull
    private CustomerTownDto town;

    public CustomerSeedDto() {
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

    public String getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(String registeredOn) {
        this.registeredOn = registeredOn;
    }

    public CustomerTownDto getTown() {
        return town;
    }

    public void setTown(CustomerTownDto town) {
        this.town = town;
    }
}
