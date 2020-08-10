package com.fp.oauth2.configuration.services.repository;

import com.fp.oauth2.configuration.services.domain.OauthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientRepo extends JpaRepository<OauthClientDetails, Long> {
    public OauthClientDetails findByClientId(String clientId);

}
