package com.fp.oauth2.configuration.services;

import com.fp.oauth2.configuration.CustomTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private CustomTokenServices tokenServices;

    @PostMapping("/revoke")
    public String revokeToken(@RequestHeader("Authorization") String authorization) {
        if ((authorization != null && authorization.contains("Bearer")) || (authorization != null && authorization.contains("bearer"))) {
            String tokenId = authorization.substring("Bearer".length() + 1);
            boolean isRevoke = tokenServices.revokeToken(tokenId);
            if (isRevoke)
                return "Success";
            else
                return "Failed";
        }
        return "Unknown Bearer token";
    }
}
