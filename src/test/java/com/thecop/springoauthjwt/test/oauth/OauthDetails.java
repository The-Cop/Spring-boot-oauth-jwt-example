package com.thecop.springoauthjwt.test.oauth;

import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;

import java.util.Arrays;
import java.util.List;

public final class OauthDetails extends ResourceOwnerPasswordResourceDetails {

    private static final String OAUTH_TOKEN_URL = OauthTests.HOST + "/oauth/token";
    private static final String CLIENT_ID = "clientApp";
    private static final String CLIENT_PASSWORD = "123456";
    private static final String GRANT_TYPE = "password";
    private static final List<String> SCOPE = Arrays.asList("read", "write");

    public static OauthDetails newUser() {
        OauthDetails oad = new OauthDetails();
        oad.setAccessTokenUri(OAUTH_TOKEN_URL);
        oad.setClientId(CLIENT_ID);
        oad.setClientSecret(CLIENT_PASSWORD);
        oad.setUsername("user");
        oad.setPassword("user");
        oad.setGrantType(GRANT_TYPE);
        oad.setScope(SCOPE);
        return oad;
    }

    public static OauthDetails newAdmin() {
        OauthDetails oad = new OauthDetails();
        oad.setAccessTokenUri(OAUTH_TOKEN_URL);
        oad.setClientId(CLIENT_ID);
        oad.setClientSecret(CLIENT_PASSWORD);
        oad.setUsername("admin");
        oad.setPassword("admin");
        oad.setGrantType(GRANT_TYPE);
        oad.setScope(SCOPE);
        return oad;
    }

    private OauthDetails() {

    }
}
