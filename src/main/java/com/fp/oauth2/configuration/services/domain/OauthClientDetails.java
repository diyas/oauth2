
package com.fp.oauth2.configuration.services.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class OauthClientDetails {

    @Id
    @Column
    private String clientId;
    @Column
    private String resourceIds;
    @Column
    private String clientSecret;
    @Column
    private String scope;
    @Column
    private String authorizedGrantTypes;
    @Column
    private String webServerRedirectUri;
    @Column
    private String authorities;
    @Column
    private Integer accessTokenValidity;
    @Column
    private Integer refreshTokenValidity;
    @Column
    private String additionalInformation;
    @Column
    private boolean autoApprove;

}
