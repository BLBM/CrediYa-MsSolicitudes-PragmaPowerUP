package co.com.bancolombia.model.loanwithrate;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanWithRate {
    private Integer loanid;
    private Double amount;
    private LocalDate timelimit;
    private Double interestrate;

}
