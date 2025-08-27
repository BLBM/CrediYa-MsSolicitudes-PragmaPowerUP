package co.com.bancolombia.logconstants;

public interface LogConstants {
    //PROCESS
    String START_PROCESS = "Starting process";
    String END_PROCESS = "Ending process: {}";
    String ERROR_PROCESS = "Error occurred in process";
    String SUCCESSFUL_APPLICATION = "Successful application: {}";

    //CONTROLLERS
    String REQUEST_RECEIVED = "Request received: {}";

    //ADAPTERS
    String SUCCESSFUL_OPERATION = "Successful operation: {}";
    String ERROR_OPERATION = "Error occurred in operation: {}";

    //GLOBAL HANDLER
    String SERVER_ERROR = "Server error: {}";
    String DOMAIN_ERROR = "Domain error: {}";
    String TIMESTAMP_ERROR = "Timestamp error: {}";
    String STATUS_ERROR = "Status error: {}";
    String MESSAGE_ERROR = "Message error: {}";
    String APPLICATION_ERROR = "Application error: {}";
    String SERVER_ERROR_MESSAGE = "An internal error has occurred. Please try again later.";
    String DOMAIN_ERROR_MESSAGE = "Domain error";



}