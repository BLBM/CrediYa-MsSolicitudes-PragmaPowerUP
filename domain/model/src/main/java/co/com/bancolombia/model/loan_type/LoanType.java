package co.com.bancolombia.model.loan_type;
import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {

    private Integer loanTypeId;
    private String name;
    private Double minAmount;
    private Double maxAmount;
    private Double interestRate;
    private Boolean automaticValidation;


    public LoanType(Integer loanTypeId) {
        this.loanTypeId = loanTypeId;
    }


}
