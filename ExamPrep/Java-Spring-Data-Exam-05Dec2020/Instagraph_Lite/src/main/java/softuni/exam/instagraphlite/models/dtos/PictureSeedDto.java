package softuni.exam.instagraphlite.models.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.*;

public class PictureSeedDto {
    @Expose
    @NotBlank
    private String path;

    @Expose
    @DecimalMin(value = "500.00", inclusive = true)
    @DecimalMax(value = "60000.00", inclusive = true)
    @NotNull
    private Double size;

    public PictureSeedDto() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }
}
