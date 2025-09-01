package co.com.bancolombia.jwt_common;

public interface JwtMessages{
    String TOKEN_NO_FOUNDS = "no token was found";
    String TOKEN_INVALID = "invalid token";

    String TOKEN_EXPIRED = "token expired";
    String TOKEN_UNSUPPORTED= "token unsupported";
    String TOKEN_MALFORMED = "token malformed";
    String ILLEGAL_ARGS = "illegal arguments";
    String INVALID_ROL = "Invalid role in token";
    String ROL_NO_FOUNDS = "No roles found in token";
    String COMPLETE_ERROR_MESSAGES ="{ \"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\" }";
    String JJWT_ERROR_PROCESS = "Error occurred in process {}";
    String START_JJWT_PROCESS_AUTHENTICATE_MANAGER = "Init jwtAuthenticateManager";

}
