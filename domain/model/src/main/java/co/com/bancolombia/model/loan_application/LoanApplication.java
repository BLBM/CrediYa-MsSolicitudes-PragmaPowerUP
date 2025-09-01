package co.com.bancolombia.model.loan_application;

import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoanApplication {

    private Integer loanApplicationId;
    private Double amount;
    private LocalDate timeLimit;
    private String email;
    private String documentId;
    private Status status;
    private LoanType loanType;
}
