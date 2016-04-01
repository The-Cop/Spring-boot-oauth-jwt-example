package com.thecop.springoauthjwt.controller;

import com.thecop.springoauthjwt.util.OauthTokenInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SampleController {

    private static final Logger LOG = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping("/testAuthInfo")
    public String getId(OAuth2Authentication auth) {
        LOG.info("SampleController.getId");
        LOG.info("auth.getPrincipal() = {}", auth.getPrincipal());
        LOG.info("auth.getCredentials() = {}", auth.getCredentials());
        LOG.info("auth.getAuthorities() = {}", auth.getAuthorities());
        LOG.info("auth.getUserAuthentication().getPrincipal() = {}", auth.getUserAuthentication().getPrincipal());
        LOG.info("auth.getUserAuthentication().getCredentials() = {}", auth.getUserAuthentication().getCredentials());
        LOG.info("auth.getUserAuthentication().getAuthorities() = {}", auth.getUserAuthentication().getAuthorities());

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        OAuth2AccessToken token = tokenStore.readAccessToken(details.getTokenValue());
        LOG.info("token = {}", token);
        LOG.info("token expiration = {}", token.getExpiration());
        LOG.info("token ExpiresIn = {}", token.getExpiresIn());
        LOG.info("token type= {}", token.getTokenType());
        LOG.info("token scope= {}", token.getScope());
        LOG.info("token refresh = {}", token.getRefreshToken());
        Map<String, Object> additionalInfo = token.getAdditionalInformation();
        LOG.info("Got details {}", details);
        LOG.info("Got decoded details {}", details.getDecodedDetails());
        LOG.info("Got additionalInfo {}", additionalInfo);
        return "See log";
    }

    @RequestMapping(value = "/anyone", method = RequestMethod.GET)
    public OauthTokenInfo getAnyoneInfo(OAuth2Authentication auth) {
        return OauthTokenInfo.of(auth, tokenStore);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/usersonly", method = RequestMethod.GET)
    public OauthTokenInfo getUserInfo(OAuth2Authentication auth) {
        return OauthTokenInfo.of(auth, tokenStore);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/adminsonly", method = RequestMethod.GET)
    public OauthTokenInfo getAdminInfo(OAuth2Authentication auth) {
        return OauthTokenInfo.of(auth, tokenStore);
    }
}
