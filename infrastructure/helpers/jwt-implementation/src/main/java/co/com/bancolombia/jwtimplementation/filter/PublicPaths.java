package co.com.bancolombia.jwtimplementation.filter;

import java.util.Arrays;

public enum PublicPaths {

    SWAGGER_UI("/swagger-ui/**"),
    SWAGGER_API_DOCS("/v3/api-docs/**"),
    WEBJARS("/webjars/**");

    private final String path;
    PublicPaths(String path) { this.path = path; }
    public String getPath() { return path; }

    public static boolean isPublic(String requestPath) {
        return Arrays.stream(values())
                .anyMatch(p -> requestPath.startsWith(p.getPath().replace("/**", "")));
    }
}
