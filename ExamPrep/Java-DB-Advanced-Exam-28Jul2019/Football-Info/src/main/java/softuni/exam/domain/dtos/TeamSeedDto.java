package softuni.exam.domain.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "team")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamSeedDto {

    @XmlElement
    @Size(min = 3, max = 20)
    private String name;
    @XmlElement(name = "picture")
    private TeamPictureUrlDto picture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamPictureUrlDto getPicture() {
        return picture;
    }

    public void setPicture(TeamPictureUrlDto picture) {
        this.picture = picture;
    }
}
