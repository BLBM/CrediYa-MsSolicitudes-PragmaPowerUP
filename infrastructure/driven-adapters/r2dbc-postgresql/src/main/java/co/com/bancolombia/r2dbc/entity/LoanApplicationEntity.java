package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("loan_application")
public class LoanApplicationEntity {

    @Id
    @Column("loan_application_id")
    private Integer solicitudId;

    private Double amount;

    @Column("time_limit")
    private Date timeLimit;

    private String email;

    @Column("status_id")
    private Integer statusId;

    @Column("loan_type_id")
    private Integer loanTypeId;
}
