package co.com.bancolombia.model.loan_application_event;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanApplicationEvent {
    private Integer loanApplicationId;
    private String documentId;
    private String email;
    private String statusDescription;
    private String loanTypeName;
    private Double amount;
}
