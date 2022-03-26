package softuni.exam.domain.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotBlank;

public class TeamPictureDto {
    @Expose
    @NotBlank
    private String url;

    public TeamPictureDto() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
