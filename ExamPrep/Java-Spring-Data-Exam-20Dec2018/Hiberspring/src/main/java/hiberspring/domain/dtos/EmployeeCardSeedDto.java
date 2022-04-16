package hiberspring.domain.dtos;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.UniqueElements;


import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class EmployeeCardSeedDto {
    @Expose
    @NotBlank
    private String number;

    public EmployeeCardSeedDto() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeeCardSeedDto that = (EmployeeCardSeedDto) o;
        return Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
