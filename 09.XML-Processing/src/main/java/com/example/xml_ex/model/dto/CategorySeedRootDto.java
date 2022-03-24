package com.example.xml_ex.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategorySeedRootDto {

    @XmlElement(name = "category")
    private Set<CategorySeedDto> categories;

    public Set<CategorySeedDto> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategorySeedDto> categories) {
        this.categories = categories;
    }
}
