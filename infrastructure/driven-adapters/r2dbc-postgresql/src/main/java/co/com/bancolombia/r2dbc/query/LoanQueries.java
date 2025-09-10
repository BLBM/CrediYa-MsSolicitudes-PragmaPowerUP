package co.com.bancolombia.r2dbc.query;

public class LoanQueries {

    private LoanQueries() {
        throw new IllegalStateException("Utility class");
    }

    public static final String FIND_LOANS_WITH_RATE_BY_STATUS_AND_EMAIL = """
        SELECT la.loan_application_id as loanId,
               la.amount,
               la.time_limit as timeLimit,
               lt.interest_rate as interestRate
        FROM loan_application la
        JOIN loan_type lt ON la.loan_type_id = lt.loan_type_id
        WHERE la.status_id = :statusId
          AND la.email = :email
    """;
}