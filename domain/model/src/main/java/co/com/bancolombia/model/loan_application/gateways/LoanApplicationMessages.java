package co.com.bancolombia.model.loan_application.gateways;

public interface LoanApplicationMessages {




    String LOAN_APPLICATION_CREATED = "Loan Application has been created";
    String ERROR_EMAIL_NO_VALID = "Email address is not valid";
    String LOAN_TYPE_NO_VALID  = "loan type is blank";
    String LOAN_TYPE_NO_EXIST ="loan type does not exist";
    String DOCUMENT_ID_NO_VALID = "Document ID is no valid";
    String TIME_LIMIT_NO_VALID = "Time limit is no valid";
    String AMOUNT_NO_VALID = "Amount  is no valid";
    String STATUS_NO_VALID = "Status is no valid";
    String USER_NO_EXIST = "not exist user with this email";
}
