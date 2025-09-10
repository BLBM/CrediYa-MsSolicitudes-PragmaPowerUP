package co.com.bancolombia.r2dbc.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanWithRateDTO {
    private Integer loanid;
    private Double amount;
    private LocalDate timelimit;
    private Double interestrate;
}