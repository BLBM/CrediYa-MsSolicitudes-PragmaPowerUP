package co.com.bancolombia.model.loan_validation_message;
import lombok.*;




@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanValidationMessage {
    private Integer loanId;
    private Double amount;
    private Integer timeLimit;
    private String loanType;
    private String status;
    private Double interestRate;
    private String email;
    private Double baseSalary;
    private Double totalDebt;
}
