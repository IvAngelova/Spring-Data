package com.example.xml_ex.model.dto.ex3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryViewRootDto {

    @XmlElement(name = "category")
    private List<CategoryViewDto> categories;

    public List<CategoryViewDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryViewDto> categories) {
        this.categories = categories;
    }
}
