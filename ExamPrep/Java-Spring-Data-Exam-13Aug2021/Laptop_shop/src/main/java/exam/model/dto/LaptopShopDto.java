package exam.model.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LaptopShopDto {
    @Expose
    @Size(min = 4)
    @NotBlank
    private String name;

    public LaptopShopDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
