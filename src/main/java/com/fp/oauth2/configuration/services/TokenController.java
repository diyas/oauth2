package com.fp.oauth2.configuration.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(
        value = {"/oauth"},
        produces = MediaType.APPLICATION_JSON_VALUE
)
@Validated
public class TokenController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private DefaultTokenServices tokenServices;

    @RequestMapping(method = RequestMethod.DELETE, path = "/revoke")
    @ResponseStatus(HttpStatus.OK)
    public void revokeToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if ((authorization != null && authorization.contains("Bearer")) || (authorization != null && authorization.contains("bearer"))) {
            String tokenId = authorization.substring("Bearer".length() + 1);

            tokenServices.revokeToken(tokenId);
            if (tokenStore instanceof JdbcTokenStore) {
                    ((JdbcTokenStore) tokenStore).removeRefreshToken(tokenId);
            }
        }
    }
}
