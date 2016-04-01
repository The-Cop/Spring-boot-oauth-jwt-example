package com.thecop.springoauthjwt.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class OauthTokenInfo {
    private String userName;
    private Collection<String> authorities;
    private LocalDateTime expiration;
    private String type;
    private Set<String> scope;
    private Map<String, Object> rawAdditionalInfo;
    private String fullName;

    public static OauthTokenInfo of(OAuth2Authentication auth, TokenStore tokenStore) {
        OauthTokenInfo info = new OauthTokenInfo();
        if (auth == null) {
            return info;
        }
        info.userName = (String) auth.getPrincipal();
        info.authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        OAuth2AccessToken token = tokenStore.readAccessToken(details.getTokenValue());
        info.expiration = LocalDateTime.ofInstant(token.getExpiration().toInstant(), ZoneId.systemDefault());
        info.type = token.getTokenType();
        info.scope = token.getScope();
        info.rawAdditionalInfo = token.getAdditionalInformation();
        info.fullName = info.rawAdditionalInfo != null ? (String) info.rawAdditionalInfo.get("fullName") : null;
        return info;
    }

    private OauthTokenInfo() {
    }

    public String getUserName() {
        return userName;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public String getType() {
        return type;
    }

    public Set<String> getScope() {
        return scope;
    }

    public Map<String, Object> getRawAdditionalInfo() {
        return rawAdditionalInfo;
    }

    public String getFullName() {
        return fullName;
    }
}
