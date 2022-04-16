package hiberspring.domain.dtos;

public class ProductiveEmployeeDto {
    private String fullName;
    private String position;
    private String cardNumber;

    public ProductiveEmployeeDto(String fullName, String position, String cardNumber) {
        this.fullName = fullName;
        this.position = position;
        this.cardNumber = cardNumber;
    }

    public ProductiveEmployeeDto() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
