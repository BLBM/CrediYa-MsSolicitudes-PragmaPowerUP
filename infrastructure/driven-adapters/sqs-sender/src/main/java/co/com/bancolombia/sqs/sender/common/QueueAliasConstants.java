package co.com.bancolombia.sqs.sender.common;


import lombok.Getter;

@Getter
public enum QueueAliasConstants {


    LOAN_APPLICATION_EVENT("loanApplicationQueue"),
    AUTOMATIC_VALIDATE_EVENT("loanValidateQueue");
    private final String message;

    QueueAliasConstants(String message) {
        this.message = message;
    }
}
