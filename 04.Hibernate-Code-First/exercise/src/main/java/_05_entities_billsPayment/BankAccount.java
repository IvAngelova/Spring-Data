package _05_entities_billsPayment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "_05_bank_accounts")
public class BankAccount extends BillingDetail{
    @Column(name = "bank_name", nullable = false, length = 70)
    private String bankName;
    @Column(name = "swift_code", nullable = false)
    private String swiftCode;

    public BankAccount() {
    }

    public BankAccount(String number, User owner, String bankName, String swiftCode) {
        super(number, owner);
        this.bankName = bankName;
        this.swiftCode = swiftCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
