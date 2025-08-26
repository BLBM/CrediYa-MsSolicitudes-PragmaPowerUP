package co.com.bancolombia.model.loan_application;

import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplication {

    private Integer loanApplicationId;
    private Double amount;
    private Date timeLimit;
    private String email;
    private Status status;
    private LoanType loanType;
}
