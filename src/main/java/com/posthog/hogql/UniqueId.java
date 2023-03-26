package com.posthog.hogql;
import java.security.SecureRandom;
import java.util.Base64;

public final class UniqueId {

    private UniqueId() {
    }

    //threadsafe
    private static final SecureRandom secureRandom = new SecureRandom();
    //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public static String generateQueryId() {
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

}
