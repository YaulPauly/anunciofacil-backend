package com.anunciofacilbackend.configuration.security;

public final class Constants {
    private Constants(){}

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_URL = "/login";

    // Secret y expiración leídas desde variables de entorno (si no existen, valores por defecto)
    public static final String SECRET = System.getenv()
            .getOrDefault("JWT_SECRET", "MiSecretoPorDefectoCambialo12345678");
    // expiración en ms
    public static final long EXPIRATION_TIME_MS;
    static {
        String v = System.getenv().get("JWT_EXPIRATION_MS");
        long parsed = 86400000L; // 1 día por defecto
        if (v != null) {
            try { parsed = Long.parseLong(v); } catch(NumberFormatException ignored){}
        }
        EXPIRATION_TIME_MS = parsed;
    }
}
