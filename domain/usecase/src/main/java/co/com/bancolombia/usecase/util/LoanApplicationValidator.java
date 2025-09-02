package co.com.bancolombia.usecase.util;

import co.com.bancolombia.model.exception.DomainException;
import co.com.bancolombia.model.loan_application.LoanApplication;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationConstants;
import co.com.bancolombia.model.loan_application.gateways.LoanApplicationMessages;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class LoanApplicationValidator {

    public void validateLoanApplication(LoanApplication loanApplication){

        if(loanApplication.getLoanType().getLoanTypeId()== null)
            throw  new DomainException(LoanApplicationMessages.LOAN_TYPE_NO_VALID);
        if(loanApplication.getAmount()== null || loanApplication.getAmount() < LoanApplicationConstants.MIN_AMOUNT)
            throw  new DomainException(LoanApplicationMessages.AMOUNT_NO_VALID);
        if(loanApplication.getTimeLimit()==null)
            throw  new DomainException(LoanApplicationMessages.TIME_LIMIT_NO_VALID);
    }
}
