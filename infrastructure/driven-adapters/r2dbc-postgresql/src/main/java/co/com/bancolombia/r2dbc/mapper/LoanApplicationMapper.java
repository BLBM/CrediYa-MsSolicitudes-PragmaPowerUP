package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_type.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.r2dbc.entity.LoanApplicationEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanApplicationMapper {


    public LoanApplicationEntity toEntity(LoanApplication loanApplication) {
        if (loanApplication == null) {
            return null;
        }

        return LoanApplicationEntity.builder()
                .loanApplicationId(loanApplication.getLoanApplicationId())
                .amount(loanApplication.getAmount())
                .timeLimit(loanApplication.getTimeLimit())
                .documentId(loanApplication.getDocumentId())
                .email(loanApplication.getEmail())
                .statusId(extractStatusId(loanApplication.getStatus()))
                .loanTypeId(extractLoanTypeId(loanApplication.getLoanType()))
                .build();
    }

    public LoanApplication toDomain(LoanApplicationEntity entity) {
        if (entity == null) {
            return null;
        }

        return LoanApplication.builder()
                .loanApplicationId(entity.getLoanApplicationId())
                .amount(entity.getAmount())
                .timeLimit(entity.getTimeLimit())
                .documentId(entity.getDocumentId())
                .email(entity.getEmail())
                .status(entity.getStatusId() != null ? new Status(entity.getStatusId()) : null)
                .loanType(entity.getLoanTypeId() != null ? new LoanType(entity.getLoanTypeId()) : null)
                .build();
    }

    private Integer extractStatusId(Status status) {
        return status != null ? status.getStatusId() : null;
    }

    private Integer extractLoanTypeId(LoanType loanType) {
        return loanType != null ? loanType.getLoanTypeId() : null;
    }

}
