package co.com.bancolombia.jwtimplementation.filter;

import java.util.Arrays;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public enum PublicPaths {
    LOGIN("/api/v1/login"),
    SWAGGER_UI("/swagger-ui"),
    SWAGGER_UI_HTML("/swagger-ui.html"),
    SWAGGER_UI_INDEX("/swagger-ui/index.html"),
    SWAGGER_API_DOCS("/v3/api-docs"),
    WEBJARS("/webjars"),
    ACTUATOR("/actuator"),
    FAVICON("/favicon.ico");

    private final String path;

    PublicPaths(String path) {
        this.path = path;
    }

    public static boolean isPublic(String requestPath) {
        if (requestPath == null || requestPath.isEmpty()) {
            return false;
        }

        log.info("Checking path: {}", requestPath);

        boolean isPublic = Arrays.stream(values())
                .anyMatch(publicPath -> {
                    String pathPattern = publicPath.getPath();

                    if (pathPattern.equals(requestPath)) {
                        log.info("Exact match found: {} =  {}" , pathPattern , requestPath);
                        return true;
                    }

                    boolean startsWithPattern = requestPath.startsWith(pathPattern);
                    if (startsWithPattern) {
                        log.info("Prefix match found:  {}  starts with   {}" , requestPath , pathPattern);
                        return true;
                    }

                    return false;
                });

        log.info("Path {} is public:  {}"  , requestPath , isPublic);
        return isPublic;
    }
}
