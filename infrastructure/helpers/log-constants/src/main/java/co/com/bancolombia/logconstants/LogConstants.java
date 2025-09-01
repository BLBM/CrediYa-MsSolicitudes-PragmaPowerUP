package co.com.bancolombia.logconstants;

public interface LogConstants {
    //PROCESS
    String START_PROCESS = "Starting process";
    String END_PROCESS = "Ending process: {}";
    String ERROR_PROCESS = "Error occurred in process";
    String SUCCESSFUL_APPLICATION = "Successful application: {}";
    String START_JJWT_PROCESS = "Starting JJWT  process";
    //CONTROLLERS
    String REQUEST_RECEIVED = "Request received: {}";
    String REQUEST_RECEIVED_LOANS_BY_STATUS = "Requests received of loans by status: {}";
    String RESPONSE_MAPPED ="Response mapped: {}";
    String FLOW_COMPLETED_LOANS_BY_STATUS = "Flow completed with all loans with status {}";
    String ERROR_OPERATION_FIND_STATUS_BY_ID = "Error occurred in find status by id: {}";


    //ADAPTERS
    String SUCCESSFUL_OPERATION = "Successful operation: {}";
    String ERROR_OPERATION = "Error occurred in operation: {}";
    String START_PROCESS_FIND_BY_STATUS = "Starting process find by status {}";

    //GLOBAL HANDLER
    String SERVER_ERROR = "Server error: {}";
    String DOMAIN_ERROR = "Domain error: {}";
    String TIMESTAMP_ERROR = "Timestamp error: {}";
    String STATUS_ERROR = "Status error: {}";
    String MESSAGE_ERROR = "Message error: {}";
    String APPLICATION_ERROR = "Application error: {}";
    String SERVER_ERROR_MESSAGE = "An internal error has occurred. Please try again later.";
    String DOMAIN_ERROR_MESSAGE = "Domain error";
    String AUTH_ERROR = "auth error: {}";
    String AUTH_ERROR_MESSAGE_FORBIDDEN = "You don’t have permission to perform this action";
    String AUTH_ERROR_MESSAGE_UNAUTHORIZED = "bad authorized";



}