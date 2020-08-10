package com.fp.oauth2.configuration.services;

import com.fp.oauth2.configuration.services.domain.OauthClientDetails;
import com.fp.oauth2.configuration.services.repository.OauthClientRepo;
import com.fp.oauth2.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

@Service
public class ClientService implements ClientDetailsService {

    @Autowired
    private OauthClientRepo oauthClientRepo;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        OauthClientDetails oauthClientDetails = oauthClientRepo.findByClientId(clientId);
        String resourceIds = oauthClientDetails.getResourceIds();
        String scopes = oauthClientDetails.getScope();
        String grantTypes = oauthClientDetails.getAuthorizedGrantTypes();
        String authorities = oauthClientDetails.getAuthorities();
        Set<String> autoApproveScopes = Utility.setString(scopes, ",");


        BaseClientDetails base = new BaseClientDetails(oauthClientDetails.getClientId(), resourceIds, scopes, grantTypes, authorities);
        base.setClientSecret(oauthClientDetails.getClientSecret());
        base.setAccessTokenValiditySeconds(oauthClientDetails.getAccessTokenValidity());
        base.setRefreshTokenValiditySeconds(oauthClientDetails.getRefreshTokenValidity());
        base.setAdditionalInformation(new HashMap<>());
        base.setAutoApproveScopes(autoApproveScopes);
        return base;
    }
}
