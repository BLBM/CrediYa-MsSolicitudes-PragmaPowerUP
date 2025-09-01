package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("loan_application")
public class LoanApplicationEntity {

    @Id
    @Column("loan_application_id")
    private Integer loanApplicationId;

    private Double amount;

    @Column("time_limit")
    private LocalDate timeLimit;

    @Column("document_id")
    private String documentId;

    private String email;

    @Column("status_id")
    private Integer statusId;

    @Column("loan_type_id")
    private Integer loanTypeId;


}
