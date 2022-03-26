package softuni.exam.domain.dtos;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TeamNameDto {
    @Expose
    @Size(min = 3, max = 20)
    private String name;

    @Expose
    @NotNull
    private TeamPictureDto picture;

    public TeamNameDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamPictureDto getPicture() {
        return picture;
    }

    public void setPicture(TeamPictureDto picture) {
        this.picture = picture;
    }
}
