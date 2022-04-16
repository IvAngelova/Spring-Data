package hiberspring.domain.dtos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.constraints.NotBlank;

public class BranchSeedDto {
    @Expose
    @NotBlank
    private String name;
    @Expose
    @NotBlank
    private String town;

    public BranchSeedDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
