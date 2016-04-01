package com.thecop.springoauthjwt.test.oauth;

import com.thecop.springoauthjwt.configuration.ApplicationConfiguration;
import com.thecop.springoauthjwt.util.OauthTokenInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfiguration.class)
@WebIntegrationTest
public class OauthTests {

    public static final String HOST = "http://localhost:8080";
    private static final String URI = HOST + "/api";
    private final RestOperations restTemplate = new RestTemplate();
    private final RestOperations oauthRestTemplateUser = new OAuth2RestTemplate(OauthDetails.newUser());
    private final RestOperations oauthRestTemplateAdmin = new OAuth2RestTemplate(OauthDetails.newAdmin());

    @Test
    public void testAnyone() {
        ResponseEntity<OauthTokenInfo> response = restTemplate.getForEntity(URI + "/anyone", OauthTokenInfo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAnyoneUser() {
        ResponseEntity<OauthTokenInfo> response = oauthRestTemplateUser.getForEntity(URI + "/anyone", OauthTokenInfo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUserUnauthorized() {
        try {
            restTemplate.getForEntity(URI + "/usersonly", OauthTokenInfo.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
        }
    }

    @Test
    public void testAdminUnauthorized() {
        try {
            restTemplate.getForEntity(URI + "/adminsonly", OauthTokenInfo.class);
        } catch (HttpClientErrorException e) {
            assertEquals(HttpStatus.UNAUTHORIZED, e.getStatusCode());
        }
    }

    @Test
    public void testUserAuthorized() {
        ResponseEntity<OauthTokenInfo> response =
                oauthRestTemplateUser.getForEntity(URI + "/usersonly", OauthTokenInfo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        OauthTokenInfo info = response.getBody();
        assertNotNull(info.getScope());
        assertNotNull(info.getAuthorities());
        assertNotNull(info.getRawAdditionalInfo());
        assertEquals("User full name", info.getFullName());
        assertEquals("user", info.getUserName());
        assertTrue(info.getAuthorities().contains("ROLE_USER"));
        assertFalse(info.getAuthorities().contains("ROLE_ADMIN"));
        assertTrue(info.getScope().contains("read"));
        assertTrue(info.getScope().contains("write"));
    }

    @Test(expected = UserDeniedAuthorizationException.class)
    public void testUserInAdminZoneUnauthorized() {
        ResponseEntity<Object> response = oauthRestTemplateUser.getForEntity(URI + "/adminsonly", null);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    public void testAdminInUserZoneAuthorized() {
        ResponseEntity<OauthTokenInfo> response =
                oauthRestTemplateAdmin.getForEntity(URI + "/usersonly", OauthTokenInfo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        OauthTokenInfo info = response.getBody();
        assertNotNull(info.getScope());
        assertNotNull(info.getAuthorities());
        assertNotNull(info.getRawAdditionalInfo());
        assertEquals("Admin full name", info.getFullName());
        assertEquals("admin", info.getUserName());
        assertTrue(info.getAuthorities().contains("ROLE_USER"));
        assertTrue(info.getAuthorities().contains("ROLE_ADMIN"));
        assertTrue(info.getScope().contains("read"));
        assertTrue(info.getScope().contains("write"));
    }

    @Test
    public void testAdminInAdminZoneAuthorized() {
        ResponseEntity<OauthTokenInfo> response =
                oauthRestTemplateAdmin.getForEntity(URI + "/adminsonly", OauthTokenInfo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        OauthTokenInfo info = response.getBody();
        assertNotNull(info.getScope());
        assertNotNull(info.getAuthorities());
        assertNotNull(info.getRawAdditionalInfo());
        assertEquals("Admin full name", info.getFullName());
        assertEquals("admin", info.getUserName());
        assertTrue(info.getAuthorities().contains("ROLE_USER"));
        assertTrue(info.getAuthorities().contains("ROLE_ADMIN"));
        assertTrue(info.getScope().contains("read"));
        assertTrue(info.getScope().contains("write"));
    }
}
