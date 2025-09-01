package co.com.bancolombia.model.loan_application_summary;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplicationSummary {

    private Double amount;
    private Date timeLimit;
    private String loanType;
    private String status;
    private Double interestRate;

    private String name;
    private String email;
    private Double baseSalary;

    private  Double totalDebt;
}
