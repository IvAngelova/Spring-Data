package exam.model.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CustomerTownDto {
    @Expose
    @Size(min = 2)
    @NotBlank
    private String name;

    public CustomerTownDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
